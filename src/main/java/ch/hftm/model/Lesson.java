package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Lesson {
    private StringProperty _name;

    private SchoolYear _parentSchoolYear;

    public Lesson(String name) {
        _name = new SimpleStringProperty(name);
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
}
