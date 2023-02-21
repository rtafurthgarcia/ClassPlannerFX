package ch.hftm.component;

import ch.hftm.model.Classroom;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
        FileViewer source = ((FileViewer) event.getGestureSource());

        Dragboard db = event.getDragboard();
        boolean success = false;
        if (!db.hasContent(DataFormat.lookupMimeType("application/json"))) return;
        
        if (! target.getChildren().contains(source)) {
            success = true;
        }

        if (success) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
    }

    private static void onDragDroppedContainer(DragEvent event, FileViewerContainer target) {
        Dragboard db = event.getDragboard();
        if (! db.hasContent(DataFormat.lookupMimeType("application/json"))) return;

        FileViewer source = ((FileViewer) event.getGestureSource());
        FileViewerContainer parent = (FileViewerContainer) source.getParent();

        boolean success = true;

        parent.getChildren().remove(source);
        target.getChildren().add(source);

        source.getCompetency().setParentClassroom(parent.getClassroom());
        source.getCompetency().setParentSchoolYearQuarter(parent.getQuarter());
        source.getCompetency().setParentThematicAxis(parent.getThematicAxis());

        event.setDropCompleted(success);
    }
   
}
