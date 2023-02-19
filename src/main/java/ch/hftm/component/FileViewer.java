package ch.hftm.component;

import java.io.IOException;
import org.hildan.fxgson.FxGson;

import com.google.gson.Gson;

import ch.hftm.ClassPlannerFX;
import ch.hftm.model.CoreCompetency;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
    }

    public BooleanProperty isFillerProperty() {
        return this.isFillerProperty();
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

    private void dragDetected(MouseEvent event, FileViewer draggedFileViewer) {
        // root can't be dragged
        if (draggedFileViewer.getParent() == null)
            return;
        Dragboard db = draggedFileViewer.startDragAndDrop(TransferMode.MOVE);

        // to also handle the Color & Font classes
        Gson gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String json = gson.toJson(draggedFileViewer.getUserData());

        ClipboardContent content = new ClipboardContent();
        content.put(DataFormat.lookupMimeType("application/json"), json);
        db.setContent(content);
        db.setDragView(draggedFileViewer.snapshot(null, null));
        event.consume();
    }

    @FXML
    private void initialize() {
        tpDescription.setOnDragDetected(event -> dragDetected(event, this));
    }
}
