package ch.hftm.component;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;

import ch.hftm.ClassPlannerFX;
import ch.hftm.model.Context;
import ch.hftm.model.CoreCompetency;
import ch.hftm.util.FileCell;
import ch.hftm.util.OSHelper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

public class FileViewer extends Accordion {
    private Context sharedContext = Context.getInstance();

    @FXML
    private TitledPane tpDescription;
    @FXML
    private TextArea taDescription;
    @FXML
    private TitledPane tpFiles;
    @FXML
    private ListView<File> lvFiles;

    private ObjectProperty<CoreCompetency> competency = new SimpleObjectProperty<>();

    public FileViewer(CoreCompetency competency) {
        super();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClassPlannerFX.class.getResource("view/FileViewerView.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
            lvFiles.setCellFactory(param -> new FileCell());
            lvFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setCompetency(competency);

            taDescription.setPrefHeight(Integer.MAX_VALUE);
            lvFiles.setPrefHeight(Integer.MAX_VALUE);

            setContextMenu(createContextMenu());
        } catch (IOException | CloneNotSupportedException exception) {
            sharedContext.getLogger().log(Level.SEVERE, exception.getLocalizedMessage());
        }
    }

    public CoreCompetency getCompetency() {
        return competency.get();
    }

    public FileViewer setCompetency(CoreCompetency competency) throws CloneNotSupportedException {
        this.competency.set(competency);

        tpDescription.textProperty().bindBidirectional(this.competency.get().nameProperty());
        taDescription.textProperty().bindBidirectional(this.competency.get().descriptionProperty());
        lvFiles.setItems(competency.filesProperty());
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

        ClipboardContent content = new ClipboardContent();
        content.put(DataFormat.PLAIN_TEXT, draggedFileViewer.getCompetency().getName());
        db.setContent(content);
        db.setDragView(draggedFileViewer.snapshot(null, null));
        event.consume();
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem miAddfile = new MenuItem("Add a file");
        MenuItem miOpenFile = new MenuItem("Open the selected file(s)");
        MenuItem miDeleteFile = new MenuItem("Remove the selected file(s) from this core competency");
        MenuItem miDelete = new MenuItem("Delete the core competency");

        SeparatorMenuItem smi = new SeparatorMenuItem();
        SeparatorMenuItem smi2 = new SeparatorMenuItem();

        miAddfile.setOnAction(event -> addFilePerFileChooser());
        miDeleteFile.setOnAction(event -> onDeleteSelectedFiles());
        miOpenFile.setOnAction(event -> openFileWithAssociatedProgram());
        
        miDelete.setOnAction(event -> {
            FileViewerContainer fileViewerContainer = (FileViewerContainer) this.getParent();
            fileViewerContainer.getChildren().remove(this);
        });

        if (lvFiles.getSelectionModel().getSelectedItems().isEmpty()) { 
            contextMenu.getItems().addAll(miAddfile, smi2);
        } else {
            contextMenu.getItems().addAll(miAddfile, smi2, miOpenFile, miDeleteFile, smi);
        }

        contextMenu.getItems().add(miDelete);

        return contextMenu;
    }

    private void onMouseClicked(MouseEvent event) {
        setContextMenu(createContextMenu());
    }

    private void openFileWithAssociatedProgram() {
        lvFiles.getSelectionModel().getSelectedItems().forEach(OSHelper::run);
    }

    private void addFilePerFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file you want to add to this core competency");
        File chosenFile = fileChooser.showOpenDialog(this.getScene().getWindow());
        if (chosenFile != null) {
            getCompetency().getFiles().add(chosenFile);
        }
    }

    private void onDeleteSelectedFiles() {
        Alert alert = new Alert(
                AlertType.CONFIRMATION,
                "Do you really want to remove the selected files from this core competency?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setHeaderText("File removal");
        alert.setTitle("Core competency: removal?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.YES) {
            getCompetency().getFiles().removeAll(lvFiles.getSelectionModel().getSelectedItems());
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

        db.getFiles().forEach(getCompetency().getFiles()::add);
        
        event.setDropCompleted(success);
    }

    @FXML
    private void initialize() {
        tpDescription.setOnDragDetected(event -> dragDetected(event, this));
        lvFiles.setOnMouseClicked(event -> onMouseClicked(event));
        lvFiles.setOnDragOver(event -> onDragOverFileArea(event));
        lvFiles.setOnDragDropped(event -> onDragDroppedFileArea(event));
    }
}
