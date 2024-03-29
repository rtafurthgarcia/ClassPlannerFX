package ch.hftm.util;

import java.util.Objects;
import java.util.logging.Level;

import ch.hftm.model.Context;
import ch.hftm.model.CoreCompetency;
import ch.hftm.model.Lesson;
import ch.hftm.model.ThematicAxis;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

public class TextFieldTreeCellFactory<T> implements Callback<TreeView<T>, TreeCell<T>> {
    private static final String CSS_SELECTION_CLASS = "selected-treeitem";
    private static final String CSS_DROP_HINT = "drop-under";
    private TreeCell<T> dropZone;
    private TreeItem<T> source;

    private Context sharedContext = Context.getInstance();

    @Override
    public TreeCell<T> call(TreeView<T> treeView) {
        TreeCell<T> cell = new TreeCell<T>() {
            private TextField textField;

            /**
             * On editing, create new text field and set it using setGraphic method.
             */
            @Override
            public void startEdit() {
                if (getItem() instanceof ThematicAxis
                        || getItem() instanceof CoreCompetency
                        || getItem() instanceof Lesson) {
                    super.startEdit();

                    if (textField == null) {
                        createTextField();
                    }
                    setText(null);
                    setGraphic(textField);
                    textField.selectAll();

                }
            }

            /**
             * On cancel edit, set the original content back
             */
            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(((T) getItem()).toString());
                setGraphic(getTreeItem().getGraphic());
            }

            @Override
            public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                if (!isEditing()) {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                    return;
                }

                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);

            }

            private void createTextField() {
                textField = new TextField(getString());
                textField.setOnKeyReleased((KeyEvent t) -> {
                    if (t.getCode() == KeyCode.ENTER) {
                        if (getItem() instanceof ThematicAxis) {
                            ThematicAxis toUpdateObject = (ThematicAxis) getItem();
                            toUpdateObject.setName(textField.getText());
                            commitEdit(super.getItem());
                        }

                        if (getItem() instanceof CoreCompetency) {
                            CoreCompetency toUpdateObject = (CoreCompetency) getItem();
                            toUpdateObject.setName(textField.getText());
                            commitEdit(super.getItem());
                        }

                        if (getItem() instanceof Lesson) {
                            Lesson toUpdateObject = (Lesson) getItem();
                            toUpdateObject.setName(textField.getText());
                            commitEdit(super.getItem());
                        }

                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                });
            }

            private String getString() {
                return getItem() == null ? "" : getItem().toString();
            }
        };
        cell.setOnDragDetected((MouseEvent event) -> {
            try {
                onDragDetected(event, cell);
            } catch (CloneNotSupportedException exception) {
                sharedContext.getLogger().log(Level.WARNING, exception.getLocalizedMessage());
            }
        });
        cell.setOnDragDone((DragEvent event) -> clearDropLocation());
        cell.setOnDragOver(event -> onDragOver(event, cell));
        cell.setOnDragDropped(event -> onDrop(event, cell));

        // sucks ass but works so ig im not gonna change anything
        // sucks ass due to the fact it requires so many listeners. it saturates memory
        // for nothing.
        sharedContext.selectedLessonProperty()
                .addListener((observable, oldValue, newValue) -> setBoldIfSelected(cell, newValue));
        sharedContext.selectedSchoolYearProperty()
                .addListener((observable, oldValue, newValue) -> setBoldIfSelected(cell, newValue));
        cell.itemProperty().addListener((observable, oldValue, newValue) -> {
            setBoldIfSelected(cell, sharedContext.getSelectedLesson());
            setBoldIfSelected(cell, sharedContext.getSelectedSchoolYear());
        });

        return cell;
    }

    private void setBoldIfSelected(TreeCell<?> cell, Object selectedValue) {
        if (cell.getItem() == null || selectedValue == null) {
            return;
        }

        if (cell.getItem().getClass().equals(selectedValue.getClass())) {
            if (((T) cell.getItem()).equals(selectedValue)) {
                cell.getStyleClass().add(CSS_SELECTION_CLASS);
            } else {
                cell.getStyleClass().removeAll(CSS_SELECTION_CLASS);
            }
        }
    }

    private void onDragDetected(MouseEvent event, TreeCell<T> treeCell) throws CloneNotSupportedException {
        source = treeCell.getTreeItem();

        // root can't be dragged
        if (source.getParent() == null && !(source.getValue() instanceof CoreCompetency))
            return;
        Dragboard db = treeCell.startDragAndDrop(TransferMode.MOVE);

        CoreCompetency clone = (CoreCompetency) ((CoreCompetency) source.getValue()).clone();

        ClipboardContent content = new ClipboardContent();
        content.put(DataFormat.PLAIN_TEXT, clone.getName());
        db.setContent(content);
        db.setDragView(treeCell.snapshot(null, null));
        event.consume();
    }

    private void onDragOver(DragEvent event, TreeCell<T> target) {
        if (!event.getDragboard().hasContent(DataFormat.PLAIN_TEXT)) return;
        TreeItem<T> thisItem = target.getTreeItem();
     
        // can't drop on itself nor can drop on something different than CoreCompetency
        if (source == null || thisItem == null || thisItem == source 
        || !(target.getItem() instanceof CoreCompetency || target.getItem() instanceof ThematicAxis
        ) || !(source.getValue() instanceof CoreCompetency)) return;
        // ignore if this is the root
        if (source.getParent() == null) {
            clearDropLocation();
            return;
        }
     
        event.acceptTransferModes(TransferMode.MOVE);
        if (!Objects.equals(dropZone, target)) {
            clearDropLocation();
            this.dropZone = target;
            dropZone.getStyleClass().add(CSS_DROP_HINT);
        }
    }

    private void onDrop(DragEvent event, TreeCell<T> target) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (!db.hasContent(DataFormat.PLAIN_TEXT)) return;

        TreeItem<T> targetItem = target.getTreeItem();
        CoreCompetency coreCompetencySource = (CoreCompetency) source.getValue();
        ThematicAxis targetThematicAxis = null;

        if (targetItem.getParent().getValue() instanceof ThematicAxis) {
            targetThematicAxis = (ThematicAxis) targetItem.getParent().getValue();
        } else if ((ThematicAxis) targetItem.getValue() instanceof ThematicAxis){
            targetThematicAxis = (ThematicAxis) targetItem.getValue();
        }

        if (targetThematicAxis != null && ! targetThematicAxis.isEqualCoreCompetencyInside(coreCompetencySource) 
            && !targetThematicAxis.equals(coreCompetencySource.getIntersection().getThematicAxis())) {
                success = targetThematicAxis.copyInsideIfNecessary(coreCompetencySource) != null;
            }
            
        event.setDropCompleted(success);
        /*// remove from previous location
        droppedItemParent.getChildren().remove(draggedItem);

        // dropping on parent node makes it the first child
        if (Objects.equals(droppedItemParent, thisItem)) {
            thisItem.getChildren().add(0, draggedItem);
            treeView.getSelectionModel().select(draggedItem);
        }
        else {
            // add to new location
            int indexInParent = thisItem.getParent().getChildren().indexOf(thisItem);
            thisItem.getParent().getChildren().add(indexInParent + 1, draggedItem);
        }
        treeView.getSelectionModel().select(draggedItem);
        event.setDropCompleted(success);*/
    }

    private void clearDropLocation() {
        if (dropZone != null)
            dropZone.getStyleClass().remove(CSS_DROP_HINT);
    }
}