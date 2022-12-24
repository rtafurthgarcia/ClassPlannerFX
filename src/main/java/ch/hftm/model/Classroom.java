package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Classroom {
    private StringProperty _name;

    private SchoolYearQuarter _parentSchoolYearQuarter;

    public Classroom(String name) {
        _name = new SimpleStringProperty(name);
    }

    public StringProperty nameProperty() {
        return _name;
    }

    public void setName(StringProperty name) {
        this._name = name;
    }

    public String getName() {
        return _name.get();
    }
}
