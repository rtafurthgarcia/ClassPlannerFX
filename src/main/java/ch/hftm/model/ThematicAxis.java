package ch.hftm.model;

import javafx.collections.FXCollections;

public class ThematicAxis extends SchoolUnit<CoreCompetency>  {
    public ThematicAxis(String name) {
        super(name, FXCollections.observableArrayList(), CoreCompetency::new);
    }
}
