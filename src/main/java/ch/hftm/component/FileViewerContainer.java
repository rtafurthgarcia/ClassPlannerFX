package ch.hftm.component;

import java.util.function.BiPredicate;

import ch.hftm.model.Classroom;
import ch.hftm.model.CoreCompetency;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
import ch.hftm.util.TextFieldTreeCellFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeCell;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

public class FileViewerContainer extends VBox {

    private ObjectProperty<Classroom> classroom = new SimpleObjectProperty<>();
    private ObjectProperty<SchoolYearQuarter> quarter = new SimpleObjectProperty<>();
    private ObjectProperty<ThematicAxis> thematicAxis = new SimpleObjectProperty<>();

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
    }
    
    private static void onDragOverContainer(DragEvent event, FileViewerContainer target) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (!db.hasContent(DataFormat.lookupMimeType("application/json"))) return;
        
        if (event.getGestureSource() instanceof FileViewer) {
            FileViewer source = ((FileViewer) event.getGestureSource());
            if (! target.getChildren().contains(source)) {
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

    private static void onDragDroppedContainer(DragEvent event, FileViewerContainer target) {
        Dragboard db = event.getDragboard();
        if (! db.hasContent(DataFormat.lookupMimeType("application/json"))) return;
        boolean success = false;

        if (event.getGestureSource() instanceof FileViewer) {
            FileViewer source = ((FileViewer) event.getGestureSource());
            FileViewerContainer parent = (FileViewerContainer) source.getParent();
    
            parent.getChildren().remove(source);
            target.getChildren().add(source);
    
            source.getCompetency().setParentClassroom(parent.getClassroom());
            source.getCompetency().setParentSchoolYearQuarter(parent.getQuarter());
            source.getCompetency().setParentThematicAxis(parent.getThematicAxis());

            success = true;
        } else if (event.getGestureSource() instanceof TreeCell) {
            CoreCompetency source = (CoreCompetency) ((TreeCell) event.getGestureSource()).getItem();
            
            target.getChildren().add(new FileViewer().setCompetency(source));

            source.setParentClassroom(target.getClassroom());
            source.setParentSchoolYearQuarter(target.getQuarter());
            source.setParentThematicAxis(target.getThematicAxis());

            success = true;
        }

        event.setDropCompleted(success);
    }
   
}
