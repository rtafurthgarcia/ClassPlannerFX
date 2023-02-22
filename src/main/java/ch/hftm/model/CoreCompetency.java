package ch.hftm.model;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class CoreCompetency extends SchoolUnit<SchoolUnit<?>> implements Cloneable {

    public CoreCompetency(String name) {
        super(name, FXCollections.observableArrayList());
    }

    private StringProperty description = new SimpleStringProperty();

    private Classroom parentClassroom;
    private ThematicAxis parentThematicAxis;
    private SchoolYearQuarter parentSchoolYearQuarter;

    public CoreCompetency setDescription(String description) {
        this.description = new SimpleStringProperty(description);

        return this;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get(); 
    }

    public Classroom getParentClassroom() {
        return parentClassroom;
    }

    public CoreCompetency setParentClassroom(Classroom parentClassroom) {
        this.parentClassroom = parentClassroom;

        return this;
    }

    public ThematicAxis getParentThematicAxis() {
        return parentThematicAxis;
    }

    public CoreCompetency setParentThematicAxis(ThematicAxis parentThematicAxis) {
        this.parentThematicAxis = parentThematicAxis;

        return this;
    }

    public SchoolYearQuarter getParentSchoolYearQuarter() {
        return parentSchoolYearQuarter;
    }

    public CoreCompetency setParentSchoolYearQuarter(SchoolYearQuarter parentSchoolYearQuarter) {
        this.parentSchoolYearQuarter = parentSchoolYearQuarter;

        return this;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        CoreCompetency competency = new CoreCompetency(this.getName()).setDescription(this.getDescription());
        
        return competency;
    }
}
