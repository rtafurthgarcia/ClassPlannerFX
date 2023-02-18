package ch.hftm.component;

import java.io.IOException;

import ch.hftm.ClassPlannerFX;
import ch.hftm.model.CoreCompetency;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.TilePane;

public class FileViewer extends Accordion {
    @FXML
    private TitledPane tpDescription;
    @FXML
    private TextArea taDescription;
    @FXML
    private TitledPane tpFiles;
    @FXML
    private TilePane tpFileArea;    
    
    private ObjectProperty<CoreCompetency> competency = new SimpleObjectProperty<>();

    public FileViewer() {
        super();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClassPlannerFX.class.getResource("view/FileViewerView.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {   
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
                
        //    Parent root = FXMLLoader.load(ClassPlannerFX.class.getResource("view/FileViewerView.fxml"));
    }

    public CoreCompetency getCompetency() {
        return competency.get();
    }

    public FileViewer setCompetency(CoreCompetency competency) {
        this.competency.set(competency);

        tpDescription.setText(competency.getName());
        taDescription.setText(competency.getDescription());

        return this;
    }

    public ObjectProperty<CoreCompetency> competencyProperty() {
        return competency;
    }
}
