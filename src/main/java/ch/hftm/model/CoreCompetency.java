package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CoreCompetency {
    private StringProperty _name;
    private StringProperty _description;

    private Classroom _parentClassroom;
    private ThematicAxis _parentThematicAxis;
    private SchoolYearQuarter _parentSchoolYearQuarter;

    private Integer _order;

    public StringProperty nameProperty() {
        return _name;
    }

    public String getName() {
        return _name.getValue();
    }

    public CoreCompetency setDescription(String description) {
        _description = new SimpleStringProperty(description);

        return this;
    }

    public CoreCompetency setName(String name) {
        _name = new SimpleStringProperty(name);

        return this;
    }

    public StringProperty descriptionProperty() {
        return _description;
    }

    public String getDescription() {
        return _description.get(); 
    }

    public Classroom getParentClassroom() {
        return _parentClassroom;
    }

    public CoreCompetency setParentClassroom(Classroom parentClassroom) {
        this._parentClassroom = parentClassroom;

        return this;
    }

    public ThematicAxis getParentThematicAxis() {
        return _parentThematicAxis;
    }

    public CoreCompetency setParentThematicAxis(ThematicAxis parentThematicAxis) {
        this._parentThematicAxis = parentThematicAxis;

        return this;
    }

    public SchoolYearQuarter getParentSchoolYearQuarter() {
        return _parentSchoolYearQuarter;
    }

    public CoreCompetency setParentSchoolYearQuarter(SchoolYearQuarter parentSchoolYearQuarter) {
        this._parentSchoolYearQuarter = parentSchoolYearQuarter;

        return this;
    }

    public Integer getOrder() {
        return _order;
    }

    public CoreCompetency setOrder(Integer order) {
        this._order = order;

        return this;
    }
}
