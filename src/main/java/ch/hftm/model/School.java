package ch.hftm.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class School extends SchoolUnit<SchoolYear> {
    private ObservableList<Classroom> classrooms;

    public ObservableList <Classroom> getClassrooms() {
        return classrooms;
    }

    public School(String name) {
        super(name, FXCollections.observableArrayList(), SchoolYear::new);

        classrooms = FXCollections.observableArrayList();
    }
}
