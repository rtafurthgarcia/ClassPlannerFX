package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ThematicAxis extends SchoolUnit<CoreCompetency>  {
    public ThematicAxis(String name) {
        super(name, FXCollections.observableArrayList(), CoreCompetency::new);
    }

    public String toString() {
        return super.getName();
    }
}
