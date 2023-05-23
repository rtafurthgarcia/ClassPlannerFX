package ch.hftm.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.logging.Level;
import java.util.stream.Collectors;

import ch.hftm.ClassPlannerFX;
import ch.hftm.component.FileViewer;
import ch.hftm.component.FileViewerContainer;
import ch.hftm.model.Context;
import ch.hftm.model.CoreCompetency;
import ch.hftm.model.Lesson;
import ch.hftm.model.School;
import ch.hftm.model.SchoolUnit;
import ch.hftm.model.SchoolYear;
import ch.hftm.model.ThematicAxis;
import ch.hftm.util.GridPaneHelper;
import ch.hftm.util.GridPaneHelper.ComponentsColumn;
import ch.hftm.util.GridPaneHelper.ComponentsRow;
import ch.hftm.util.ModelTree;
import ch.hftm.util.TextFieldTreeCellFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainViewController {
    @FXML
    private GridPane gpMain;

    @FXML
    private AnchorPane apTreeView;

    private TreeView<SchoolUnit<?>> twSchoolYearPlan;

    private Context sharedContext = Context.getInstance();

    private Integer counter;

    private ArrayList<ComponentsRow> graphicalRows = new ArrayList<>();
    private ArrayList<ComponentsColumn> componentsColumns = new ArrayList<>();
    private ArrayList<FileViewerContainer> fileViewerContainers = new ArrayList<>();

    @FXML
    private MenuItem miSave;

    EventHandler<ActionEvent> onAddLesson = new EventHandler<>() {
        public void handle(ActionEvent e) {
            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof Lesson) {
                twSchoolYearPlan.getSelectionModel().getSelectedItem().getParent().getValue()
                        .createAndAddSubUnit("new lesson");
            }

            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof SchoolYear) {
                twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue().createAndAddSubUnit("new lesson");
            }
        }
    };

    EventHandler<ActionEvent> onAddThematicAxis = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            ThematicAxis newThematicAxis = new ThematicAxis("new thematic axis");

            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof ThematicAxis) {
                ((Lesson) twSchoolYearPlan.getSelectionModel().getSelectedItem().getParent().getValue()).getSubUnits()
                        .add(newThematicAxis);
            }

            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof Lesson) {
                ((Lesson) twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue()).getSubUnits()
                        .add(newThematicAxis);
            }

            graphicalRows.add(GridPaneHelper.addGridRow(gpMain, newThematicAxis, componentsColumns));
        }
    };

    EventHandler<ActionEvent> onAddCoreCompetency = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            CoreCompetency newCoreCompetency = new CoreCompetency("new core competency");

            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof CoreCompetency) {
                ThematicAxis selectedThematicAxis = ((ThematicAxis) twSchoolYearPlan.getSelectionModel()
                        .getSelectedItem().getParent().getValue());
                selectedThematicAxis.getSubUnits().add(newCoreCompetency);
            }

            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof ThematicAxis) {
                ThematicAxis selectedThematicAxis = ((ThematicAxis) twSchoolYearPlan.getSelectionModel()
                        .getSelectedItem().getValue());
                selectedThematicAxis.getSubUnits().add(newCoreCompetency);
            }
        }
    };

    EventHandler<ActionEvent> onLoadSelection = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof Lesson) {
                sharedContext
                        .setSelectedLesson((Lesson) twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue());
            } else if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof SchoolYear) {
                sharedContext.setSelectedSchoolYear(
                        (SchoolYear) twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue());
                sharedContext.setSelectedLesson(null);
            }
            clear();

            try {
                initialize();
            } catch (NullPointerException | IOException exception) {
                sharedContext.getLogger().log(Level.SEVERE, exception.getLocalizedMessage());
            }
        }
    };

    EventHandler<ActionEvent> onDeleteSchoolUnit = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getChildren().size() == 0) {
                SchoolUnit<?> value = twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue();
                if (value instanceof ThematicAxis) {
                    GridPaneHelper.removeGridRow(gpMain, (ThematicAxis) value, graphicalRows);
                } else if (value instanceof CoreCompetency) {
                    ThematicAxis parentThematicAxis = (ThematicAxis) twSchoolYearPlan.getSelectionModel()
                            .getSelectedItem().getParent().getValue();
                    // we have to delete all core components
                    List<FileViewer> list = fileViewerContainers.stream()
                            .flatMap(c -> c.getChildren().stream())
                            .map(n -> {
                                return (FileViewer) n;
                            })
                            .filter(f -> f.getCompetency().equals((CoreCompetency) value)
                                    && f.getCompetency().getIntersection().getThematicAxis().equals(parentThematicAxis))
                            .collect(Collectors.toList());

                    list.forEach(f -> {
                        FileViewerContainer fileViewerContainer = (FileViewerContainer) f.getParent();
                        fileViewerContainer.getChildren().removeAll(list);
                    });

                }
                // TODO: should I really keep it???
                // wont it remove things I dont want to???
                twSchoolYearPlan.getSelectionModel().getSelectedItem().getParent().getValue().getSubUnits()
                        .remove(value);
            } else {
                Alert alert = new Alert(
                        AlertType.INFORMATION,
                        "Cannot delete item when it has children. Please make sure to remove all sub items beforehand.");
                alert.setHeaderText("Deletion impossible");
                alert.setTitle("Item deletion");
                alert.showAndWait();
            }
        }
    };

    public static MainViewController showMainView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClassPlannerFX.class.getResource("view/MainView.fxml"));

        // Show the scene containing the root layout.
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();

        return loader.getController();
    }

    @FXML
    public void initialize() throws IOException {
        gpMain.setGridLinesVisible(false);

        generateColumns();

        GridPaneHelper.addGridHeaderRow(gpMain, List.of("Semestre 1", "Semestre 2"), 4, 1);
        GridPaneHelper.addGridHeaderRow(gpMain, List.of("Trimestre 1", "Trimestre 2", "Trimestre 3", "Trimestre 4"), 2,
                1);
        GridPaneHelper.addGridHeaderRow(
                gpMain,
                sharedContext.getSelectedSchoolYear().getQuarters()
                        .sorted((q1, q2) -> q1.getQuarter().compareTo(q2.getQuarter())),
                2,
                1);

        int i = 0;
        int rowIndex = gpMain.getRowCount();
        gpMain.getRowConstraints().add(new RowConstraints(30));
        while (i < sharedContext.getLoadedSchool().getClassrooms().size()
                * sharedContext.getSelectedSchoolYear().getQuarters().size()) {
            for (int j = 0; j < sharedContext.getLoadedSchool().getClassrooms().size(); j++) {
                GridPaneHelper.addGridHeaderRow(
                        gpMain,
                        List.of(sharedContext.getLoadedSchool().getClassrooms().get(j)),
                        1,
                        1 + i,
                        rowIndex);

                i++;
            }
        }
        loadThematicAxises();
        loadCoreCompetencies();

        loadTreeView();
    }

    void generateColumns() {
        counter = 0;
        sharedContext.getSelectedSchoolYear().getQuarters().forEach(quarter -> {
            sharedContext.getLoadedSchool().getClassrooms().forEach(classroom -> {
                componentsColumns.add(counter, new ComponentsColumn(quarter, classroom));
                counter++;
            });
        });

        counter = 0;

        final int COLUMN_COUNT = componentsColumns.size() + 1; // + 1 -> thematic axis column

        gpMain.getRowConstraints().clear();
        gpMain.getColumnConstraints().clear();

        for (int i = 0; i < COLUMN_COUNT; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setFillWidth(true);
            cc.setHgrow(Priority.ALWAYS);

            gpMain.getColumnConstraints().add(cc);
        }
    }

    void loadTreeView() {
        ModelTree<SchoolUnit<?>> tree = new ModelTree<>(sharedContext.getLoadedSchool(),
                SchoolUnit::getSubUnits,
                SchoolUnit::nameProperty,
                unit -> PseudoClass.getPseudoClass(unit.getClass().getSimpleName().toLowerCase()));

        twSchoolYearPlan = tree.getTreeView();

        apTreeView.getChildren().add(twSchoolYearPlan);

        twSchoolYearPlan.prefHeightProperty().bind(gpMain.heightProperty());
        twSchoolYearPlan.setEditable(true);
        twSchoolYearPlan.setCellFactory(new TextFieldTreeCellFactory<SchoolUnit<?>>());
        twSchoolYearPlan.setOnMouseClicked(event -> {
            twSchoolYearPlan.setContextMenu(createTreeViewContextMenu());
        });
    }

    public void loadThematicAxises() {
        sharedContext.getSelectedLesson().getSubUnits().forEach(thematicAxis -> {
            ComponentsRow row = GridPaneHelper.addGridRow(gpMain, thematicAxis, componentsColumns);
            fileViewerContainers.addAll(row.containers());
            graphicalRows.add(row);
        });
    }

    void loadCoreCompetencies() {
        BiPredicate<CoreCompetency, FileViewerContainer> predicate = (cc,
                c) -> c.getIntersection().equals(cc.getIntersection());

        fileViewerContainers.stream()
                .filter(c -> sharedContext.getSelectedLesson().getSubUnits().stream()
                        .flatMap(t -> t.getSubUnits().stream())
                        .anyMatch(cc -> predicate.test(cc, c)))
                .collect(Collectors.toList())
                .forEach(c -> {
                    c.getChildren().add(
                        new FileViewer(
                                sharedContext.getSelectedLesson().getSubUnits().stream()
                                    .flatMap(t -> t.getSubUnits().stream())
                                    .filter(cc -> predicate.test(cc, c))
                                    .findFirst()
                                    .get()
                                    .cloneForGrid()));
                });
    }

    private ContextMenu createTreeViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        if (twSchoolYearPlan.getSelectionModel().getSelectedItem() != null) {
            MenuItem miAddThematicAxis = new MenuItem("Create a new thematic axis");
            MenuItem miDeleteThematicAxis = new MenuItem("Delete selected thematic axis");
            MenuItem miAddLesson = new MenuItem("Create a new lesson");
            MenuItem miDeleteLesson = new MenuItem("Delete selected lesson");
            MenuItem miLoadLesson = new MenuItem("Load selected lesson");
            MenuItem miAddCoreCompetency = new MenuItem("Create a new core competency");
            MenuItem miDeleteCoreCompetency = new MenuItem("Delete selected core competency");
            MenuItem miLoadSchoolYear = new MenuItem("Load selected school year");
            MenuItem miCreateNewYear = new MenuItem("Create a new school year");

            SeparatorMenuItem smiSeparator = new SeparatorMenuItem();
            SeparatorMenuItem smiSeparator2 = new SeparatorMenuItem();

            miAddThematicAxis.setOnAction(onAddThematicAxis);
            miAddLesson.setOnAction(onAddLesson);
            miAddCoreCompetency.setOnAction(onAddCoreCompetency);

            miDeleteThematicAxis.setOnAction(onDeleteSchoolUnit);
            miDeleteLesson.setOnAction(onDeleteSchoolUnit);
            miDeleteCoreCompetency.setOnAction(onDeleteSchoolUnit);

            miLoadLesson.setOnAction(onLoadSelection);
            miLoadSchoolYear.setOnAction(onLoadSelection);

            // miCreateNewYear.setOnAction(onAddNewYear);

            if (twSchoolYearPlan.getSelectionModel().getSelectedItem() == twSchoolYearPlan.getRoot()) {
                contextMenu.getItems().addAll(miCreateNewYear);
            } else if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof SchoolYear) {
                contextMenu.getItems().addAll(miCreateNewYear, smiSeparator2, miLoadSchoolYear, smiSeparator,
                        miAddLesson);
            } else if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof Lesson) {
                contextMenu.getItems().addAll(miLoadLesson, smiSeparator2, miAddLesson, miAddThematicAxis, smiSeparator,
                        miDeleteLesson);
            } else if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof ThematicAxis) {
                contextMenu.getItems().addAll(miAddThematicAxis, miAddCoreCompetency, smiSeparator,
                        miDeleteThematicAxis);
            } else if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof CoreCompetency) {
                contextMenu.getItems().addAll(miAddCoreCompetency, smiSeparator, miDeleteCoreCompetency);
            }
        }

        return contextMenu;
    }

    public void clear() {
        gpMain.getChildren().clear();
        gpMain.getRowConstraints().clear();
        gpMain.getColumnConstraints().clear();
        graphicalRows.clear();
        componentsColumns.clear();
        fileViewerContainers.clear();
    }

    @FXML
    void onQuit() {
        Context.getInstance().getPrimaryStage().close();
    }

    @FXML
    public void onOpenSettings() throws IOException {
        sharedContext.setSecondaryStage(SettingsController.showSettingsView());

        clear();
        initialize();
    }

    @FXML
    public void onOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a ClassPlannerFX school file");
        File selectedFile = fileChooser.showOpenDialog(sharedContext.getPrimaryStage());

        if (selectedFile != null) {
            try {
                JAXBContext context = JAXBContext.newInstance(School.class);    
                sharedContext.setLoadedSchool((School) context.createUnmarshaller().unmarshal(selectedFile));    
                sharedContext.setSelectedSchoolYear(sharedContext.getLoadedSchool().getSubUnits().stream().findFirst().get());
                sharedContext.setSelectedLesson(sharedContext.getLoadedSchool().getSubUnits().stream().findFirst().get().getSubUnits().stream().findFirst().get());

                clear();
                initialize();

            } catch (IOException | JAXBException exception) {
                sharedContext.getLogger().log(Level.SEVERE, exception.getLocalizedMessage());

                Alert alert = new Alert(
                        AlertType.ERROR,
                        exception.getLocalizedMessage());
                alert.setHeaderText("Impossible to open school file");
                alert.setTitle("File opening failed");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void onSaveAs() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensions = new FileChooser.ExtensionFilter("ClassPlannerFX files (*.cpf)",
                "*.cpf");
        fileChooser.getExtensionFilters().add(extensions);
        fileChooser.setTitle("Chose the directory and the filename of your savefile");
        fileChooser.setInitialFileName("school.cpf");

        File file = fileChooser.showSaveDialog(sharedContext.getPrimaryStage());
        String filePath = file.getAbsolutePath();

        saveCurrentSchoolFile(filePath);
        sharedContext.setSaveFilePath(filePath);
    }

    @FXML
    public void onSave() {
        if (sharedContext.getSaveFilePath().isEmpty()) {
            onSaveAs();
        } else {
            saveCurrentSchoolFile(sharedContext.getSaveFilePath());
        }
    }

    public void saveCurrentSchoolFile(String filePath) {
        /*
         * try {
         * String json =
         * sharedContext.getSerializer().toJson(sharedContext.getLoadedSchool());
         * BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
         * writer.write(json);
         * writer.close();
         * } catch (JsonIOException | IOException exception) {
         * sharedContext.getLogger().log(Level.SEVERE, exception.getLocalizedMessage());
         * }
         */
        try {
            JAXBContext context = JAXBContext.newInstance(School.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(sharedContext.getLoadedSchool(), new File(filePath));
        } catch (JAXBException exception) {
            sharedContext.getLogger().log(Level.SEVERE, exception.getLocalizedMessage());

            Alert alert = new Alert(
                        AlertType.ERROR,
                        exception.getLocalizedMessage());
                alert.setHeaderText("Impossible to save school file");
                alert.setContentText(exception.getLocalizedMessage());
                alert.setTitle("File saving failed");
                alert.showAndWait();
        }
    }

    public GridPane getGpMain() {
        return gpMain;
    }

    public AnchorPane getApTreeView() {
        return apTreeView;
    }

    public TreeView<SchoolUnit<?>> getTwSchoolYearPlan() {
        return twSchoolYearPlan;
    }

    public ArrayList<ComponentsRow> getGraphicalRows() {
        return graphicalRows;
    }

    public ArrayList<ComponentsColumn> getComponentsColumns() {
        return componentsColumns;
    }

    public ArrayList<FileViewerContainer> getFileViewerContainers() {
        return fileViewerContainers;
    }
}