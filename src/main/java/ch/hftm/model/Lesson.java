package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Lesson {
    private StringProperty _name;

    private SchoolYear _parentSchoolYear;

    public SchoolYear getSchoolYear() {
        return _parentSchoolYear;
    }

    public void setParentSchoolYear(SchoolYear schoolYear) {
        this._parentSchoolYear = schoolYear;
    }

    public Lesson(String name, SchoolYear schoolYear) {
        _name = new SimpleStringProperty(name);
        _parentSchoolYear = schoolYear;
    }

    public StringProperty nameProperty() {
        return _name;
    }

    public String getName() {
        return _name.get();
    }

    public void setName(StringProperty name) {
        this._name = name;
    }

    public String toString() {
        return _name.get();
    }
}
