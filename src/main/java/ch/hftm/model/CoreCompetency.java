package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class CoreCompetency extends SchoolUnit<SchoolUnit<?>> {

    public CoreCompetency(String name) {
        super(name, FXCollections.observableArrayList());
        //TODO Auto-generated constructor stub
    }

    //private StringProperty _name;
    private StringProperty _description;

    private Classroom _parentClassroom;
    private ThematicAxis _parentThematicAxis;
    private SchoolYearQuarter _parentSchoolYearQuarter;

    private Integer _order;

    public CoreCompetency setDescription(String description) {
        _description = new SimpleStringProperty(description);

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
