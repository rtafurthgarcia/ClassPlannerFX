package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public class Lesson {
    private StringProperty _name;

    private ObservableSet<ThematicAxis> _lessonsAxis;

    public ObservableSet<ThematicAxis> getLessonsAxis() {
        return _lessonsAxis;
    }

    public Lesson setLessonsAxis(ObservableSet<ThematicAxis> lessonsAxis) {
        this._lessonsAxis = lessonsAxis;

        return this;
    }

    public Lesson(String name) {
        _name = new SimpleStringProperty(name);

        _lessonsAxis = FXCollections.observableSet();
    }

    public StringProperty nameProperty() {
        return _name;
    }

    public String getName() {
        return _name.get();
    }

    public Lesson setName(String name) {
        this._name = new SimpleStringProperty(name);

        return this;
    }

    public String toString() {
        return _name.get();
    }
}
