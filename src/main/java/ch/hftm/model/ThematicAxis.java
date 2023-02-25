package ch.hftm.model;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ThematicAxis extends SchoolUnit<CoreCompetency>  {
    public ThematicAxis(String name) {
        super(name, FXCollections.observableArrayList(), CoreCompetency::new);
    }

    public boolean copyInsideIfNecessary(CoreCompetency source) {
        boolean hadToCopy = false;

        String contextText = String.format(
                    "This one core competency is originally part of thematic axis '%s'. \nDo you want to create a copy that is part of '%s'?",
                    source.getParentThematicAxis(),
                    super.getName());

        Alert alert = new Alert(
                AlertType.CONFIRMATION,
                contextText,
                ButtonType.YES,
                ButtonType.NO);
        alert.setHeaderText("Core competency");
        alert.setTitle("Core competency: new copy?");
        Optional<ButtonType> result = alert.showAndWait();
        hadToCopy = (result.isPresent() && result.get() == ButtonType.YES);

        if (hadToCopy) {
            this.getSubUnits().add(source.clone()/* .setParentThematicAxis(this)*/);
        }

        return hadToCopy;
    }

    public boolean isEqualCoreCompetencyInside(CoreCompetency source) {
        boolean isPresent = false;

        isPresent = this.getSubUnits().stream()
            .anyMatch(c -> c.equals(source));

        return isPresent;
    }
}
