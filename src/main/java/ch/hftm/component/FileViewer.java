package ch.hftm.component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;


import ch.hftm.ClassPlannerFX;
import ch.hftm.model.Context;
import ch.hftm.model.CoreCompetency;
import ch.hftm.util.OSHelper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
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
    private TilePane tpFileArea;
    @FXML
    private Text tIndication;

    private ObjectProperty<CoreCompetency> competency = new SimpleObjectProperty<>();

    private List<FileItem> selectedFileItems = new ArrayList<>();

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

            getCompetency().getFiles().addListener((Change<? extends File> c) -> {
                if (getCompetency().getFiles().size() > 0) {
                    tpFileArea.getChildren().remove(tIndication);
                } else {
                    tpFileArea.getChildren().add(tIndication);
                }
           });
        } catch (IOException | CloneNotSupportedException exception) {
            sharedContext.getLogger().log(Level.SEVERE, exception.toString());
        }
    }

    public CoreCompetency getCompetency() {
        return competency.get();
    }

    public FileViewer setCompetency(CoreCompetency competency) throws CloneNotSupportedException {
        this.competency.set(competency);

        tpDescription.textProperty().bindBidirectional(this.competency.get().nameProperty());
        taDescription.textProperty().bindBidirectional(this.competency.get().descriptionProperty());

        if (competency.getFiles().size() > 0) {
            competency.getFiles().forEach(f -> {
                try {
                    tpFileArea.getChildren().add(new FileItem(f));
                } catch (IOException exception) {
                    sharedContext.getLogger().log(Level.SEVERE, exception.toString());
                }
            });

            tpFileArea.getChildren().remove(tIndication);
        } 

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
        /*Gson gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String json = gson.toJson(draggedFileViewer.getCompetency());*/

        ClipboardContent content = new ClipboardContent();
        //content.put(DataFormat.lookupMimeType("application/json"), json);
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

        if (! selectedFileItems.isEmpty()) {
            contextMenu.getItems().addAll(miAddfile, smi2, miOpenFile, miDeleteFile, smi);
        } else {
            contextMenu.getItems().addAll(miAddfile, smi2);
        }
        
        contextMenu.getItems().add(miDelete);

        return contextMenu;
    }

    private void onMouseClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (getCompetency().getFiles().isEmpty()) {
                addFilePerFileChooser();
            } else {
                selectedFileItems = tpFileArea.getChildren().stream()
                    .filter(n -> n instanceof FileItem) 
                    .map(n -> ((FileItem) n))
                    .filter(n -> n.isSelected())
                    .collect(Collectors.toList());
            }
        }

        setContextMenu(createContextMenu());
    }

    private void openFileWithAssociatedProgram() {
        selectedFileItems.forEach(f -> {
            OSHelper.run(f.getFile());
        });
    }

    private void addFilePerFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file you want to add to this core competency");
        File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
        if (selectedFile != null) {
            try {
                tpFileArea.getChildren().add(new FileItem(selectedFile));

                getCompetency().getFiles().add(selectedFile);
            } catch (IOException exception) {
                sharedContext.getLogger().log(Level.SEVERE, exception.toString());
            }
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
            selectedFileItems.forEach(f -> {
                tpFileArea.getChildren().remove(f);
                getCompetency().getFiles().remove(f);
            });
    
            selectedFileItems.clear();
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
            try {
                tpFileArea.getChildren().add(new FileItem(f));
                getCompetency().getFiles().add(f);
            } catch (IOException exception) {
                sharedContext.getLogger().log(Level.INFO, exception.toString());
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
        //tpFileArea.setOnMouseEntered(event -> setContextMenu(createContextMenu()));
    }
}
