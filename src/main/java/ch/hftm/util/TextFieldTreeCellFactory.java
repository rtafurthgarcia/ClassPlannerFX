package ch.hftm.util;

import java.util.Objects;

import org.hildan.fxgson.FxGson;

import com.google.gson.Gson;

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
    private static final DataFormat JSON_FORMAT = new DataFormat("application/json");
    private static final String DROP_HINT_STYLE = "-fx-border-color: #eea82f; -fx-border-width: 0 0 2 0; -fx-padding: 3 3 1 3";
    private TreeCell<T> dropZone;
    private TreeItem<T> draggedItem;

    private TextField textField;

    @Override
    public TreeCell<T> call(TreeView<T> treeView) {
        TreeCell<T> cell = new TreeCell<T>() {

            /**
             * On editing, create new text field and set it using setGraphic method.
             */
            @Override
            public void startEdit() {
                if (getItem() instanceof ThematicAxis || getItem() instanceof CoreCompetency
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
                setText((String) getItem());
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
        cell.setOnDragDetected((MouseEvent event) -> dragDetected(event, cell));
        cell.setOnDragOver((DragEvent event) -> dragOver(event, cell));
        cell.setOnDragDropped((DragEvent event) -> drop(event, cell, treeView));
        cell.setOnDragDone((DragEvent event) -> clearDropLocation());

        return cell;
    }


    private void dragDetected(MouseEvent event, TreeCell<T> treeCell) {
        draggedItem = treeCell.getTreeItem();

        // root can't be dragged
        if (draggedItem.getParent() == null) return;
        Dragboard db = treeCell.startDragAndDrop(TransferMode.MOVE);

        // to also handle the Color & Font classes
        //Gson fxGsonWithExtras = FxGson.createWithExtras();
        Gson gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String json = gson.toJson(draggedItem.getValue());

        ClipboardContent content = new ClipboardContent();
        content.put(JSON_FORMAT, json);
        db.setContent(content);
        db.setDragView(treeCell.snapshot(null, null));
        event.consume();
    }

    private void dragOver(DragEvent event, TreeCell<T> treeCell) {
        if (!event.getDragboard().hasContent(JSON_FORMAT)) return;
        TreeItem<T> thisItem = treeCell.getTreeItem();

        // can't drop on itself
        if (draggedItem == null || thisItem == null || thisItem == draggedItem) return;
        // ignore if this is the root
        if (draggedItem.getParent() == null) {
            clearDropLocation();
            return;
        }

        event.acceptTransferModes(TransferMode.MOVE);
        if (!Objects.equals(dropZone, treeCell)) {
            clearDropLocation();
            this.dropZone = treeCell;
            dropZone.setStyle(DROP_HINT_STYLE);
        }
    }

    private void drop(DragEvent event, TreeCell<T> treeCell, TreeView<T> treeView) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (!db.hasContent(JSON_FORMAT)) return;

        TreeItem<T> thisItem = treeCell.getTreeItem();
        TreeItem<T> droppedItemParent = draggedItem.getParent();

        // remove from previous location
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
        event.setDropCompleted(success);
    }

    private void clearDropLocation() {
        if (dropZone != null) dropZone.setStyle("");
    }
}