package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Lesson extends SchoolUnit<ThematicAxis> {
    public Lesson(String name) {
        super(name, FXCollections.observableArrayList(), ThematicAxis::new);
    }

    public String toString() {
        return super.getName();
    }
}
