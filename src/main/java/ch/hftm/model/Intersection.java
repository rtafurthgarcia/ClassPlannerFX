package ch.hftm.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Intersection {
    private ObjectProperty<Classroom> classroom = new SimpleObjectProperty<>();
    private ObjectProperty<SchoolYearQuarter> quarter = new SimpleObjectProperty<>();
    private ObjectProperty<ThematicAxis> thematicAxis = new SimpleObjectProperty<>();

    public Intersection(Classroom classroom, SchoolYearQuarter quarter, ThematicAxis thematicAxis) {
        this.classroom.set(classroom);
        this.quarter.set(quarter);
        this.thematicAxis.set(thematicAxis);
    }

    public Intersection() {

    }

    public ThematicAxis getThematicAxis() {
        return thematicAxis.get();
    }

    public Intersection setThematicAxis(ThematicAxis thematicAxis) {
        this.thematicAxis.set(thematicAxis);

        return this;
    }

    public ObjectProperty<ThematicAxis> thematicAxisProperty() {
        return thematicAxis;
    }

    public SchoolYearQuarter getQuarter() {
        return quarter.get();
    }

    public Intersection setQuarter(SchoolYearQuarter quarter) {
        this.quarter.set(quarter);

        return this;
    }

    public ObjectProperty<SchoolYearQuarter> quarterProperty() {
        return quarter;
    }

    public Classroom getClassroom() {
        return classroom.get();
    }

    public Intersection setClassroom(Classroom classroom) {
        this.classroom.set(classroom);

        return this;
    }

    public ObjectProperty<Classroom> classroomProperty() {
        return classroom;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classroom == null) ? 0 : classroom.get().hashCode());
        result = prime * result + ((quarter == null) ? 0 : quarter.get().hashCode());
        result = prime * result + ((thematicAxis == null) ? 0 : thematicAxis.get().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Intersection other = (Intersection) obj;
        if (classroom == null) {
            if (other.classroom != null)
                return false;
        } else if (!classroom.get().equals(other.classroom.get()))
            return false;
        if (quarter == null) {
            if (other.quarter != null)
                return false;
        } else if (!quarter.get().equals(other.quarter.get()))
            return false;
        if (thematicAxis == null) {
            if (other.thematicAxis != null)
                return false;
        } else if (!thematicAxis.get().equals(other.thematicAxis.get()))
            return false;
        return true;
    }
}
