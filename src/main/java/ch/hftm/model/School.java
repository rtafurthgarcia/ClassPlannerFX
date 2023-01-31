package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public class School {
    StringProperty _name;

    private ObservableSet<SchoolYear> _schoolYears;
    private ObservableSet<Classroom> _classrooms;

    public ObservableSet<Classroom> getClassrooms() {
        return _classrooms;
    }

    public ObservableSet<SchoolYear> getSchoolYears() {
        return _schoolYears;
    }

    public School(String name) {
        this._name = new SimpleStringProperty(name);

        _schoolYears = FXCollections.observableSet();
        _classrooms = FXCollections.observableSet();
    }

    public StringProperty nameProperty() {
        return _name;
    }

    public School setName(String name) {
        this._name = new SimpleStringProperty(name);

        return this;
    }

    public String getName() {
        return _name.get(); 
    }
}
