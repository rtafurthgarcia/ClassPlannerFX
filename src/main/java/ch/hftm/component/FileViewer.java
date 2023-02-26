package ch.hftm.component;

import java.io.File;
import java.io.IOException;
import org.hildan.fxgson.FxGson;

import com.google.gson.Gson;

import ch.hftm.ClassPlannerFX;
import ch.hftm.model.CoreCompetency;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileViewer extends Accordion {
    @FXML
    private TitledPane tpDescription;
    @FXML
    private TextArea taDescription;
    @FXML
    private TitledPane tpFiles;
    @FXML
    private TilePane tpFileArea;
    @FXML
    private Text tIndication;

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

            taDescription.setPrefHeight(Integer.MAX_VALUE);
            tpFileArea.setPrefHeight(Integer.MAX_VALUE);

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

    private void onMouseClicked(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
        if (selectedFile != null) {
            try {
                tpFileArea.getChildren().remove(tIndication);
                tpFileArea.getChildren().add(new FileItem(selectedFile));

                getCompetency().getFiles().add(selectedFile);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void onDragOverFileArea(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = db.hasFiles();

        if (success) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
    }

    private void onDragDroppedFileArea(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = db.hasFiles();

        db.getFiles().forEach(f -> {
            tpFileArea.getChildren().remove(tIndication);
            try {
                tpFileArea.getChildren().add(new FileItem(f));
                getCompetency().getFiles().add(f);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
        event.setDropCompleted(success);
    }

    @FXML
    private void initialize() {
        tpDescription.setOnDragDetected(event -> dragDetected(event, this));
        tpFileArea.setOnMouseClicked(event -> onMouseClicked(event));
        tpFileArea.setOnDragOver(event -> onDragOverFileArea(event));
        tpFileArea.setOnDragDropped(event -> onDragDroppedFileArea(event));
    }
}
