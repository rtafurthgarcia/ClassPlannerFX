package ch.hftm.model;

import javafx.collections.FXCollections;

public class Lesson extends SchoolUnit<ThematicAxis> {
    public Lesson(String name) {
        super(name, FXCollections.observableArrayList(), ThematicAxis::new);
    }
}
