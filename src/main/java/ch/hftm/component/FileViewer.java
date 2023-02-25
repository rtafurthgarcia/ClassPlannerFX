package ch.hftm.component;

import java.io.IOException;
import org.hildan.fxgson.FxGson;

import com.google.gson.Gson;

import ch.hftm.ClassPlannerFX;
import ch.hftm.model.CoreCompetency;
import ch.hftm.model.Lesson;
import ch.hftm.model.SchoolYear;
import ch.hftm.model.ThematicAxis;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
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

    public FileViewer(CoreCompetency competency) {
        super();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClassPlannerFX.class.getResource("view/FileViewerView.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
            this.setCompetency(competency);
            this.setContextMenu(createContextMenu());
        } catch (IOException | CloneNotSupportedException exception) {
            throw new RuntimeException(exception);
        }
    }

    public CoreCompetency getCompetency() {
        return competency.get();
    }

    public FileViewer setCompetency(CoreCompetency competency) throws CloneNotSupportedException {
        this.competency.set(competency);

        tpDescription.textProperty().bindBidirectional(this.competency.get().nameProperty());
        taDescription.textProperty().bindBidirectional(this.competency.get().descriptionProperty());

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

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem miDelete = new MenuItem("Delete this one core competency");

        miDelete.setOnAction(event -> {
            FileViewerContainer fileViewerContainer = (FileViewerContainer) this.getParent();
            fileViewerContainer.getChildren().remove(this);
        });
        
        contextMenu.getItems().add(miDelete);

        return contextMenu;
    }

    @FXML
    private void initialize() {
        tpDescription.setOnDragDetected(event -> dragDetected(event, this));
    }
}
