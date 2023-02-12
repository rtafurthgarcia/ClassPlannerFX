package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class CoreCompetency extends SchoolUnit<SchoolUnit<?>> {

    public CoreCompetency(String name) {
        super(name, FXCollections.observableArrayList());
    }

    private StringProperty description;

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
}
