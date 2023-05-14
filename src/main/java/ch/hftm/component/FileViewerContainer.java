package ch.hftm.component;

import ch.hftm.model.CoreCompetency;
import ch.hftm.model.Intersection;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.TreeCell;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class FileViewerContainer extends VBox {
    public static final String CSS_CLASS = "file-viewer-container";
    public static final String CSS_CLASS_RIGHT = "file-viewer-container-right";

    private Intersection intersection;
    
    public FileViewerContainer() {
        super();
        
        this.intersection = new Intersection();

        this.setOnDragOver(event -> onDragOverContainer(event, this));
        this.setOnDragDropped(event -> onDragDroppedContainer(event, this));
        this.getStyleClass().add(CSS_CLASS);
        
        this.setOnMouseEntered(event -> onMouseEntered(this));
        this.setOnMouseExited(event -> onMouseExited(this));
        // oddity I dont quite understand: I cant use calculated values such as USE_COMPUTED_SIZE & co
        // had to set weird values aswell in the pref heights from my controls aswell
        this.setPrefHeight(Integer.MAX_VALUE);
        this.setPrefWidth(Integer.MAX_VALUE);
        this.setMinWidth(10);
    }

    public void setIntersection(Intersection intersection) {
        this.intersection = intersection;
    }

    public Intersection getIntersection() {
        return intersection;
    }
    
    private static void onDragOverContainer(DragEvent event, FileViewerContainer target) {
        boolean success = false;

        if (event.getGestureSource() instanceof FileViewer) {
            FileViewer source = ((FileViewer) event.getGestureSource());
            if (!target.getChildren().contains(source)) {
                success = true;
            }
        } else if (event.getGestureSource() instanceof TreeCell) {
            CoreCompetency source = (CoreCompetency) ((TreeCell<?>) event.getGestureSource()).getItem();

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

    
    private static void onDragDroppedContainer(DragEvent event, FileViewerContainer target) {
        boolean success = false;

        // when moving core competency between tiles
        if (event.getGestureSource() instanceof FileViewer) {
            FileViewer source = ((FileViewer) event.getGestureSource());
            FileViewerContainer parent = (FileViewerContainer) source.getParent();
            
            boolean shouldContinue = true;
            if (!target.getIntersection().getThematicAxis().isEqualCoreCompetencyInside(source.getCompetency()) && !target.getIntersection().getThematicAxis().equals(source.getCompetency().getIntersection().getThematicAxis())) {
                shouldContinue = target.getIntersection().getThematicAxis().copyInsideIfNecessary(source.getCompetency());
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
                source.getCompetency().getIntersection().getThematicAxis().getSubUnits().removeIf(
                    c -> (c.equals(source.getCompetency()) && ! c.isPartOfTreeView())
                );
                
                target.getChildren().add(source);
                
                source.getCompetency().setIntersection(target.getIntersection());
                success = true;
            }
            // when drag and dropping a core competency from the treeview to the grid
        } else if (event.getGestureSource() instanceof TreeCell) {
            CoreCompetency source = ((CoreCompetency) ((TreeCell<?>) event.getGestureSource()).getItem()).clone();
            
            boolean shouldContinue = true;
            if (! target.getIntersection().getThematicAxis().isEqualCoreCompetencyInside(source) && !target.getIntersection().getThematicAxis().equals(source.getIntersection().getThematicAxis())) {
                shouldContinue = target.getIntersection().getThematicAxis().copyInsideIfNecessary(source);
            }

            if (shouldContinue) {
                source.setIntersection(target.getIntersection());
                source.setPartOfTreeView(false);
                
                target.getChildren().add(new FileViewer(source));
                
                success = true;
            }
        }

        event.setDropCompleted(success);
    }

    private static void onMouseEntered(FileViewerContainer target) {
        if (target.getChildren().size() > 0) {
            int columnIndex = GridPane.getColumnIndex(target);
            if (((GridPane) target.getParent()).getColumnCount() > columnIndex + 1) {
                int rowIndex = GridPane.getRowIndex(target);
                target.toFront();
        
                GridPane.setConstraints(target, columnIndex, rowIndex, 2, 1, HPos.CENTER, VPos.CENTER);
            }
        }
    }
    
    private static void onMouseExited(FileViewerContainer target) {
        if (target.getChildren().size() > 0) {
            int columnIndex = GridPane.getColumnIndex(target);
            int rowIndex = GridPane.getRowIndex(target);
    
            GridPane.setConstraints(target, columnIndex, rowIndex, 1, 1, HPos.CENTER, VPos.CENTER);
        }
    }
}
