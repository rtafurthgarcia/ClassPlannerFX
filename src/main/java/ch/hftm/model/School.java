package ch.hftm.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@XmlRootElement(name = "school")
public class School extends SchoolUnit<SchoolYear> {
    private ObservableList<Classroom> classrooms;
    
    @XmlElementWrapper(name = "classrooms")
    @XmlElements({
        @XmlElement(name="classroom", type=Classroom.class)
    })
    public ObservableList<Classroom> getClassrooms() {
        return classrooms;
    }

    public School(String name) {
        super(name, FXCollections.observableArrayList(), SchoolYear::new);

        classrooms = FXCollections.observableArrayList();
    }

    public School() {
        super("null", FXCollections.observableArrayList(), SchoolYear::new);

        classrooms = FXCollections.observableArrayList();
    }
}
