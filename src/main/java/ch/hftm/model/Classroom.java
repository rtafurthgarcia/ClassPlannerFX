package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Classroom {
    private StringProperty name;

    private SchoolYearQuarter parentSchoolYearQuarter;

    public Classroom(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Classroom setName(StringProperty name) {
        this.name = name;

        return this;
    }

    public String getName() {
        return name.get();
    }
}
