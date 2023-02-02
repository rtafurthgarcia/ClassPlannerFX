package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class School extends SchoolUnit<SchoolYear> {
    private ObservableList<Classroom> _classrooms;

    public ObservableList<Classroom> getClassrooms() {
        return _classrooms;
    }

    public School(String name) {
        super(name, FXCollections.observableArrayList(), SchoolYear::new);

        _classrooms = FXCollections.observableArrayList();
    }
}
