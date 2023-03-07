package ch.hftm.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.collections.FXCollections;

@XmlRootElement(name = "lesson")
public class Lesson extends SchoolUnit<ThematicAxis> {
    public Lesson(String name) {
        super(name, FXCollections.observableArrayList(), ThematicAxis::new);
    }

    public Lesson() {
        super("null", FXCollections.observableArrayList(), ThematicAxis::new);
    }
}
