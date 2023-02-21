package ch.hftm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.controlsfx.control.spreadsheet.SpreadsheetView;

import ch.hftm.component.FileViewer;

import ch.hftm.model.Context;
import ch.hftm.model.CoreCompetency;
import ch.hftm.model.Lesson;
import ch.hftm.model.SchoolUnit;
import ch.hftm.model.SchoolYear;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
import ch.hftm.util.GridPaneHelper;
import ch.hftm.util.ModelTree;
import ch.hftm.util.TextFieldTreeCellFactory;
import ch.hftm.util.GridPaneHelper.ComponentsColumn;
import ch.hftm.util.GridPaneHelper.ComponentsRow;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MainViewController {

    @FXML
    private SpreadsheetView svMain;

    @FXML
    private GridPane gpMain;

    @FXML
    private AnchorPane apTreeView;

    private TreeView<SchoolUnit<?>> twSchoolYearPlan;

    private Context sharedContext = Context.getInstance();

    private Integer counter;

    private ArrayList<ComponentsRow> graphicalRows = new ArrayList<>();
    private ArrayList<ComponentsColumn> graphicalColumns = new ArrayList<>();

    EventHandler<ActionEvent> onAddLesson = new EventHandler<>() {
        public void handle(ActionEvent e) {               
            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof Lesson) {
                twSchoolYearPlan.getSelectionModel().getSelectedItem().getParent().getValue().createAndAddSubUnit("new lesson");
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
                ((Lesson)twSchoolYearPlan.getSelectionModel().getSelectedItem().getParent().getValue()).getSubUnits().add(newThematicAxis);
            } 

            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof Lesson) {
                ((Lesson)twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue()).getSubUnits().add(newThematicAxis);
            }

            graphicalRows.add(GridPaneHelper.addGridRow(gpMain, newThematicAxis, graphicalColumns));
        }
    };

    EventHandler<ActionEvent> onAddCoreCompetency = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {    
            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof CoreCompetency) {
                twSchoolYearPlan.getSelectionModel().getSelectedItem().getParent().getValue().createAndAddSubUnit("new core competency");
            } 

            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof ThematicAxis) {
                twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue().createAndAddSubUnit("new core competency");
            }
        }
    };

    EventHandler<ActionEvent> onLoadSelection = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof Lesson) {
                sharedContext.setSelectedLesson((Lesson) twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue());
            } else if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof SchoolYear) {
                sharedContext.setSelectedSchoolYear((SchoolYear) twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue());
            }
        }
    };

    EventHandler<ActionEvent> onDeleteSchoolUnit = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {   
            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getChildren().size() == 0) { 
                SchoolUnit<?> value = twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue();
                if (value instanceof ThematicAxis) {
                    GridPaneHelper.removeGridRow(gpMain, (ThematicAxis) value, graphicalRows);
                }
                twSchoolYearPlan.getSelectionModel().getSelectedItem().getParent().getValue().getSubUnits().remove(value); 
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

    @FXML
    public void initialize() throws IOException {
        generateGraphicalColumns();

        setGridConstraints();
        setSemestres();
        setQuarters();
        setQuartersWithWeeks();
        setClassrooms();
        setThematicAxis();

        loadTreeView();

        //loadGrid();

        /*sharedContext.getSelectedSchoolYear().getSubUnits().addListener((ListChangeListener<Lesson>)(c -> {
            sharedContext.getSelectedSchoolYear().getSubUnits().sort((firstLesson, secondLesson) -> {
                return firstLesson.getName().compareTo(secondLesson.getName());
            });
        }));

        sharedContext.getSelectedSchoolYear().getSubUnits().forEach(lesson -> {
            
        });*/
        
        graphicalRows.get(2).boxes().get(2).getChildren().add(new FileViewer().setCompetency(new CoreCompetency("bullshit").setDescription("teehe")));
        graphicalRows.get(1).boxes().get(3).getChildren().add(new FileViewer().setCompetency(new CoreCompetency("baka stupid").setDescription("uguu")));
    }  

    /*void loadGrid() {
        sharedContext.getSelectedLesson().getSubUnits().forEach(axis -> {
            axis.getSubUnits().forEach(competency -> {

            });
        });
    }*/

    void generateGraphicalColumns() {
        counter = 0;
        sharedContext.getSelectedSchoolYear().getQuarters().forEach(quarter -> {
            sharedContext.getLoadedSchool().getClassrooms().forEach(classroom -> {
                graphicalColumns.add(counter, new ComponentsColumn(quarter, classroom));
                counter ++;
            });
        });

        counter = 0;
    }

    void loadTreeView() {
        ModelTree<SchoolUnit<?>> tree = new ModelTree<>(sharedContext.getLoadedSchool(), 
        SchoolUnit::getSubUnits, 
        SchoolUnit::nameProperty, 
        unit -> PseudoClass.getPseudoClass(unit.getClass().getSimpleName().toLowerCase()));

        twSchoolYearPlan = tree.getTreeView();

        apTreeView.getChildren().add(twSchoolYearPlan);
        apTreeView.prefWidthProperty().bind(twSchoolYearPlan.widthProperty());
        apTreeView.prefHeightProperty().bind(twSchoolYearPlan.heightProperty());

        twSchoolYearPlan.setEditable(true);
        twSchoolYearPlan.setCellFactory(new TextFieldTreeCellFactory<SchoolUnit<?>>());
        twSchoolYearPlan.setOnMouseClicked(event -> {
            twSchoolYearPlan.setContextMenu(createContextMenu());
        });
    }

    void setGridConstraints() {
        final int ROW_COUNT = 4; // one for the trimestre, one for the semestres, one for the weeks and one for classes;
        final int COLUMN_COUNT = sharedContext.getSelectedSchoolYear().getQuarters().size() * sharedContext.getLoadedSchool().getClassrooms().size() + 1; // + 1 -> thematic axis column

        gpMain.getRowConstraints().clear();
        gpMain.getColumnConstraints().clear();

        for(int i = 0; i < COLUMN_COUNT; i ++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setFillWidth(true);
            cc.setHgrow(Priority.SOMETIMES);
            
            gpMain.getColumnConstraints().add(cc); 
        }

        
        for(int i = 0; i < ROW_COUNT; i ++) {
            gpMain.getRowConstraints().add(new RowConstraints(30));
        }
    }
    
    void setSemestres() {
        final int ROW_INDEX = 0;
        
        Text tSemestre1 = new Text("Semestre 1");
        Text tSemestre2 = new Text("Semestre 2");

        gpMain.getChildren().addAll(tSemestre1, tSemestre2);
        
        GridPane.setConstraints(tSemestre1, (graphicalColumns.size() / 4) * 1, ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(tSemestre2, (graphicalColumns.size() / 4) * 3, ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
    }

    void setQuarters() {
        final int ROW_INDEX = 1;

        List<String> list = List.of("Trimestre 1", "Trimestre 2", "Trimestre 3", "Trimestre 4");
        for(int i = 0; i < list.size(); i++) {
            Text tQuarter = new Text(list.get(i));
            gpMain.getChildren().add(tQuarter);
    
            GridPane.setConstraints(tQuarter, ((graphicalColumns.size() / 4) * i + 1), ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
        }
    }

    void setQuartersWithWeeks() {
        final int ROW_INDEX = 2;

        SortedList<SchoolYearQuarter> list = sharedContext.getSelectedSchoolYear().getQuarters().sorted((q1, q2) -> q1.getQuarter().compareTo(q2.getQuarter()));
        list.forEach(quarter -> {
            Text tQuarter = new Text(quarter.toString());
            gpMain.getChildren().add(tQuarter);

            GridPane.setConstraints(tQuarter, ((graphicalColumns.size() / 4) * quarter.getQuarter()) - 1, ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
        });
    }
    
    public void setClassrooms() {
        final int ROW_INDEX = 3;

        int COLUMN_COUNT = gpMain.getColumnCount() - 1;
        counter = 1;

        while(counter < COLUMN_COUNT) {
            sharedContext.getLoadedSchool().getClassrooms().forEach(c -> {
                Text tNewClassroom = new Text(c.getName());
                tNewClassroom.setUserData(c);

                gpMain.getChildren().add(tNewClassroom);
                GridPane.setConstraints(tNewClassroom, counter, ROW_INDEX, 1, 1, HPos.CENTER, VPos.CENTER);

                //graphicalColumns.add(counter, new ComponentsColumn(c, ))

                counter ++;
            });
        }
    }

    public void setThematicAxis() {
        sharedContext.getSelectedLesson().getSubUnits().forEach(thematicAxis -> {
            graphicalRows.add(GridPaneHelper.addGridRow(gpMain, thematicAxis, graphicalColumns));
        });
    }

    private ContextMenu createContextMenu() {
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

            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof SchoolYear) {
                contextMenu.getItems().addAll(miLoadSchoolYear, smiSeparator, miAddLesson);
            } else if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof Lesson) {
                contextMenu.getItems().addAll(miLoadLesson, smiSeparator2, miAddLesson, miAddThematicAxis, smiSeparator, miDeleteLesson);
            } else if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof ThematicAxis) {
                contextMenu.getItems().addAll(miAddThematicAxis, miAddCoreCompetency, smiSeparator, miDeleteThematicAxis);
            } else if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof CoreCompetency) {
                contextMenu.getItems().addAll(miAddCoreCompetency, smiSeparator, miDeleteCoreCompetency);
            }
        }

        return contextMenu;
    }

    @FXML
    void onClose() {
        Context.getInstance().getPrimaryStage().close();
    }
    
    @FXML
    public void onOpenSettings() {
        sharedContext.showSettingsView();
    }
}