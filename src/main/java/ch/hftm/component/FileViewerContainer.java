package ch.hftm.component;

import ch.hftm.model.Classroom;
import ch.hftm.model.CoreCompetency;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.TreeCell;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

public class FileViewerContainer extends VBox {

    private ObjectProperty<Classroom> classroom = new SimpleObjectProperty<>();
    private ObjectProperty<SchoolYearQuarter> quarter = new SimpleObjectProperty<>();
    private ObjectProperty<ThematicAxis> thematicAxis = new SimpleObjectProperty<>();

    public static final String CSS_CLASS = "file-viewer-container";
    public static final String CSS_CLASS_RIGHT = "file-viewer-container-right";

    public ThematicAxis getThematicAxis() {
        return thematicAxis.get();
    }

    public FileViewerContainer setThematicAxis(ThematicAxis thematicAxis) {
        this.thematicAxis.set(thematicAxis);

        return this;
    }

    public ObjectProperty<ThematicAxis> thematicAxisProperty() {
        return thematicAxis;
    }

    public SchoolYearQuarter getQuarter() {
        return quarter.get();
    }

    public FileViewerContainer setQuarter(SchoolYearQuarter quarter) {
        this.quarter.set(quarter);

        return this;
    }

    public ObjectProperty<SchoolYearQuarter> quarterProperty() {
        return quarter;
    }

    public Classroom getClassroom() {
        return classroom.get();
    }

    public FileViewerContainer setClassroom(Classroom classroom) {
        this.classroom.set(classroom);

        return this;
    }

    public ObjectProperty<Classroom> classroomProperty() {
        return classroom;
    }

    public FileViewerContainer() {
        super();

        this.setOnDragOver(event -> onDragOverContainer(event, this));
        this.setOnDragDropped(event -> onDragDroppedContainer(event, this));
        this.getStyleClass().add(CSS_CLASS);

        this.setOnMouseEntered(event -> onMouseEntered(event, this));
        this.setOnMouseExited(event -> onMouseExited(event, this));
        // oddity I dont quite understand: I cant use calculated values such as USE_COMPUTED_SIZE & co
        // had to set weird values aswell in the pref heights from my controls aswell
        this.setPrefHeight(Integer.MAX_VALUE);
        this.setPrefWidth(Integer.MAX_VALUE);
        this.setMinWidth(10);
    }

    private static void onDragOverContainer(DragEvent event, FileViewerContainer target) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (!db.hasContent(DataFormat.lookupMimeType("application/json")))
            return;

        if (event.getGestureSource() instanceof FileViewer) {
            FileViewer source = ((FileViewer) event.getGestureSource());
            if (!target.getChildren().contains(source)) {
                success = true;
            }
        } else if (event.getGestureSource() instanceof TreeCell) {
            CoreCompetency source = (CoreCompetency) ((TreeCell) event.getGestureSource()).getItem();

            success = !target.getChildren().stream()
                    .map(n -> {
                        return ((FileViewer) n).getCompetency();
                    })
                    .anyMatch(f -> f.equals(source));
        }

        if (success) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
    }

    private static void onMouseEntered(MouseEvent event, FileViewerContainer target) {
        if (target.getChildren().size() > 0) {
            int columnIndex = GridPane.getColumnIndex(target);
            if (((GridPane) target.getParent()).getColumnCount() > columnIndex + 1) {
                int rowIndex = GridPane.getRowIndex(target);
                target.toFront();
        
                GridPane.setConstraints(target, columnIndex, rowIndex, 2, 1, HPos.CENTER, VPos.CENTER);
            }
        }
    }

    private static void onMouseExited(MouseEvent event, FileViewerContainer target) {
        if (target.getChildren().size() > 0) {
            int columnIndex = GridPane.getColumnIndex(target);
            int rowIndex = GridPane.getRowIndex(target);

            GridPane.setConstraints(target, columnIndex, rowIndex, 1, 1, HPos.CENTER, VPos.CENTER);
        }
    }

    private static void onDragDroppedContainer(DragEvent event, FileViewerContainer target) {
        Dragboard db = event.getDragboard();
        if (!db.hasContent(DataFormat.lookupMimeType("application/json")))
            return;
        boolean success = false;

        // when moving core competency between tiles
        if (event.getGestureSource() instanceof FileViewer) {
            FileViewer source = ((FileViewer) event.getGestureSource());
            FileViewerContainer parent = (FileViewerContainer) source.getParent();

            boolean shouldContinue = true;
            if (!target.getThematicAxis().isEqualCoreCompetencyInside(source.getCompetency()) && !target.getThematicAxis().equals(source.getCompetency().getParentThematicAxis())) {
                shouldContinue = target.getThematicAxis().copyInsideIfNecessary(source.getCompetency());
            }

            if (shouldContinue) {
                shouldContinue = ! target.getChildren().stream()
                    .map(n -> {
                        return ((FileViewer) n).getCompetency();
                    })
                    .anyMatch(f -> (f.equals(source.getCompetency())));
            }
            
            if (shouldContinue) {
                parent.getChildren().remove(source);
                source.getCompetency().getParentThematicAxis().getSubUnits().removeIf(
                    c -> (c.equals(source.getCompetency()) && ! c.isPartOfTreeView())
                );

                target.getChildren().add(source);
    
                source.getCompetency().setParentThematicAxis(target.getThematicAxis());
                source.getCompetency().setParentClassroom(target.getClassroom());
                source.getCompetency().setParentSchoolYearQuarter(target.getQuarter());
                success = true;
            }
        // when drag and dropping a core competency from the treeview to the grid
        } else if (event.getGestureSource() instanceof TreeCell) {
            CoreCompetency source = ((CoreCompetency) ((TreeCell) event.getGestureSource()).getItem()).clone();
            
            boolean shouldContinue = true;
            if (! target.getThematicAxis().isEqualCoreCompetencyInside(source) && !target.getThematicAxis().equals(source.getParentThematicAxis())) {
                shouldContinue = target.getThematicAxis().copyInsideIfNecessary(source);
            }

            if (shouldContinue) {
                source.setParentThematicAxis(target.getThematicAxis());
                source.setParentClassroom(target.getClassroom());
                source.setParentSchoolYearQuarter(target.getQuarter());
                source.setPartOfTreeView(false);

                target.getChildren().add(new FileViewer(source));

                success = true;
            }
        }

        event.setDropCompleted(success);
    }

}
