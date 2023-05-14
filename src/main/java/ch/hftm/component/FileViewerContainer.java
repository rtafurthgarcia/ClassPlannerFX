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

// Necessary container for our fileviewers; is basically at the intersection of classrooms, school year quarters and thematic axis. 
public class FileViewerContainer extends VBox {
    public static final String CSS_CLASS = "file-viewer-container";
    public static final String CSS_CLASS_RIGHT = "file-viewer-container-right";

    private Intersection intersection;
    
    public FileViewerContainer() {
        super();
        
        this.intersection = new Intersection();

        this.setOnDragOver(event -> onDragOverContainer(event, this));
        this.setOnDragDropped(event -> {
            try {
                onDragDroppedContainer(event, this);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        });
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

    
    private static void onDragDroppedContainer(DragEvent event, FileViewerContainer target) throws CloneNotSupportedException {
        boolean success = false;

        // when moving core competency between tiles
        if (event.getGestureSource() instanceof FileViewer) {
            FileViewer source = ((FileViewer) event.getGestureSource());
            FileViewerContainer parent = (FileViewerContainer) source.getParent();
            
            boolean shouldContinue = true;
            CoreCompetency clone = null;

            // make a copy of our core competency inside a new thematic axis if necessary
            if (!target.getIntersection().getThematicAxis().isEqualCoreCompetencyInside(source.getCompetency()) 
            && !target.getIntersection().getThematicAxis().equals(source.getCompetency().getIntersection().getThematicAxis())) {
                clone = target.getIntersection().getThematicAxis().copyInsideIfNecessary(source.getCompetency());
                shouldContinue = clone != null;
            }

            if (shouldContinue) {
                // make sure not to copy the same core competency within the same tile
                shouldContinue = ! target.getChildren().stream()
                    .map(n -> ((FileViewer) n).getCompetency() )
                    .anyMatch(f -> (f.equals(source.getCompetency())));
            }
            
            if (shouldContinue) {
                // remove it from its previous tile
                parent.getChildren().remove(source);
                source.getCompetency().getIntersection().getThematicAxis().getSubUnits().removeIf(
                    c -> (c.equals(source.getCompetency()) && ! c.isPartOfTreeView())
                );
                
                if (clone != null) {
                    source.setCompetency(clone.clone());
                }

                // add it back within the destination
                target.getChildren().add(source);
                
                source.getCompetency().setIntersection(target.getIntersection());
                success = true;
            }
        // when drag and dropping a core competency from the treeview to the grid
        } else if (event.getGestureSource() instanceof TreeCell) {
            CoreCompetency source = ((CoreCompetency) ((TreeCell<?>) event.getGestureSource()).getItem());
            
            boolean shouldContinue = true;
            CoreCompetency clone = null;

            // make a copy of our core competency inside a new thematic axis if necessary
            if (! target.getIntersection().getThematicAxis().isEqualCoreCompetencyInside(source) 
            && !target.getIntersection().getThematicAxis().equals(source.getIntersection().getThematicAxis())) {
                clone = target.getIntersection().getThematicAxis().copyInsideIfNecessary(source);
                shouldContinue = clone != null;
            }

            if (shouldContinue) {
                CoreCompetency coreCompetencyFromTreeView = null;
                if (clone != null) {
                    coreCompetencyFromTreeView = clone.clone();
                } else {
                    coreCompetencyFromTreeView = source.clone();
                }
                
                coreCompetencyFromTreeView.setIntersection(target.getIntersection());
                coreCompetencyFromTreeView.setPartOfTreeView(false);
                target.getChildren().add(new FileViewer(coreCompetencyFromTreeView));
                
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
