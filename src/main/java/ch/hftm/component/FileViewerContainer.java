package ch.hftm.component;

import ch.hftm.model.Classroom;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.VBox;

public class FileViewerContainer extends VBox {

    private ObjectProperty<Classroom> classroom = new SimpleObjectProperty<>();
    private ObjectProperty<SchoolYearQuarter> quarter = new SimpleObjectProperty<>();
    private ObjectProperty<ThematicAxis> axis = new SimpleObjectProperty<>();

    public ThematicAxis getAxis() {
        return axis.get();
    }

    public FileViewerContainer setAxis(ThematicAxis axis) {
        this.axis.set(axis);

        return this;
    }

    public ObjectProperty<ThematicAxis> axisProperty() {
        return axis;
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
    }
    

}
