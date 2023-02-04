package ch.hftm.controller;

import org.controlsfx.control.spreadsheet.SpreadsheetView;

import ch.hftm.model.Context;
import ch.hftm.model.CoreCompetency;
import ch.hftm.model.Lesson;
import ch.hftm.model.SchoolUnit;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
import ch.hftm.util.ModelTree;
import ch.hftm.util.TextFieldTreeCellFactory;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

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

    private Context _sharedContext = Context.getInstance();

    private Integer counter;

    EventHandler<ActionEvent> onAddLesson = new EventHandler<>() {
        public void handle(ActionEvent e) {               
            _sharedContext.selectedSchoolYear.createAndAddSubUnit("new lesson");
        }
    };

    EventHandler<ActionEvent> onAddThematicAxis = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            _sharedContext.selectedLesson.createAndAddSubUnit("new thematic axis");
        }
    };

    EventHandler<ActionEvent> onAddCoreCompetency = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {    
            twSchoolYearPlan.getSelectionModel().getSelectedItem().getParent().getValue().createAndAddSubUnit("new core competency");
        }
    };

    EventHandler<ActionEvent> onDeleteSchoolUnit = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {   
            if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getChildren().size() == 0) { 
                twSchoolYearPlan.getSelectionModel().getSelectedItem().getParent().getValue().getSubUnits().remove(twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue()); 
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
    public void initialize() {
        setGridConstraints();
        setSemestres();
        setQuarters();
        setQuartersWithWeeks();
        setClassrooms();
        setThematicAxis();

        loadTreeView();
    }  

    void loadTreeView() {
        ModelTree<SchoolUnit<?>> tree = new ModelTree<>(_sharedContext.loadedSchool, 
        SchoolUnit::getSubUnits, 
        SchoolUnit::nameProperty, 
        unit -> PseudoClass.getPseudoClass(unit.getClass().getSimpleName().toLowerCase()));

        twSchoolYearPlan = tree.getTreeView();

        apTreeView.getChildren().add(twSchoolYearPlan);
        apTreeView.prefWidthProperty().bind(twSchoolYearPlan.widthProperty());
        apTreeView.prefHeightProperty().bind(twSchoolYearPlan.heightProperty());

        twSchoolYearPlan.setEditable(true);
        twSchoolYearPlan.setCellFactory(new TextFieldTreeCellFactory<SchoolUnit<?>>());
        twSchoolYearPlan.setOnContextMenuRequested(event -> twSchoolYearPlan.setContextMenu(createContextMenu()));
    }

    void setGridConstraints() {
        int rowCount =  4; // one for the trimestre, one for the semestres, one for the weeks and one for classes;
        int columnCount = _sharedContext.selectedSchoolYear.getQuarters().size() * _sharedContext.loadedSchool.getClassrooms().size() + 1; // + 1 -> thematic axis column

        gpMain.getRowConstraints().clear();
        gpMain.getColumnConstraints().clear();

        for(int i = 0; i < columnCount; i ++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setFillWidth(true);
            cc.setHgrow(Priority.SOMETIMES);
            
            gpMain.getColumnConstraints().add(cc); 
        }

        
        for(int i = 0; i < rowCount; i ++) {
            gpMain.getRowConstraints().add(new RowConstraints(30));
        }
    }
    
    void setSemestres() {
        final int ROW_INDEX = 0;
        
        int columnCount = gpMain.getColumnCount() - 1;

        Text tSemestre1 = new Text("Semestre 1");
        Text tSemestre2 = new Text("Semestre 2");

        gpMain.getChildren().addAll(tSemestre1, tSemestre2);
        
        GridPane.setConstraints(tSemestre1, (columnCount / 4) * 1, ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(tSemestre2, (columnCount / 4) * 3, ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
    }

    void setQuarters() {
        final int ROW_INDEX = 1;
        
        int columnCount = gpMain.getColumnCount() - 1;
        
        Text tQuarter1 = new Text("Trimestre 1");
        Text tQuarter2 = new Text("Trimestre 2");
        Text tQuarter3 = new Text("Trimestre 3");
        Text tQuarter4 = new Text("Trimestre 4");

        gpMain.getChildren().addAll(tQuarter1, tQuarter2, tQuarter3, tQuarter4);

        GridPane.setConstraints(tQuarter1, ((columnCount / 4) * 1) - 1, ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(tQuarter2, ((columnCount / 4) * 2) - 1, ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(tQuarter3, ((columnCount / 4) * 3) - 1, ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(tQuarter4, ((columnCount / 4) * 4) - 1, ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
    }

    void setQuartersWithWeeks() {
        final int ROW_INDEX = 2;
        
        int columnCount = gpMain.getColumnCount() - 1;

        Text tQuarter1 = new Text("");
        Text tQuarter2 = new Text("");
        Text tQuarter3 = new Text("");
        Text tQuarter4 = new Text("");

        SchoolYearQuarter quarter1 = _sharedContext.selectedSchoolYear.getQuarters().stream().filter(t -> t.getQuarter() == 1).reduce((a, b) -> {
            throw new IllegalStateException("Multiple elements: " + a + ", " + b);
        }).get();
        tQuarter1.setText(quarter1.toString());
        tQuarter1.setUserData(quarter1);
        
        SchoolYearQuarter quarter2 = _sharedContext.selectedSchoolYear.getQuarters().stream().filter(t -> t.getQuarter() == 2).reduce((a, b) -> {
            throw new IllegalStateException("Multiple elements: " + a + ", " + b);
        }).get();

        tQuarter2.setText(quarter2.toString());
        tQuarter2.setUserData(quarter2);

        SchoolYearQuarter quarter3 = _sharedContext.selectedSchoolYear.getQuarters().stream().filter(t -> t.getQuarter() == 3).reduce((a, b) -> {
            throw new IllegalStateException("Multiple elements: " + a + ", " + b);
        }).get();

        tQuarter3.setText(quarter3.toString());
        tQuarter3.setUserData(quarter3);
        
        SchoolYearQuarter quarter4 = _sharedContext.selectedSchoolYear.getQuarters().stream().filter(t -> t.getQuarter() == 4).reduce((a, b) -> {
            throw new IllegalStateException("Multiple elements: " + a + ", " + b);
        }).get();

        tQuarter4.setText(quarter4.toString());
        tQuarter4.setUserData(quarter4);

        gpMain.getChildren().addAll(tQuarter1, tQuarter2, tQuarter3, tQuarter4);
        
        GridPane.setConstraints(tQuarter1, ((columnCount / 4) * 1) - 1, ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(tQuarter2, ((columnCount / 4) * 2) - 1, ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(tQuarter3, ((columnCount / 4) * 3) - 1, ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(tQuarter4, ((columnCount / 4) * 4) - 1, ROW_INDEX, 2, 1, HPos.CENTER, VPos.CENTER);
    }
    
    public void setClassrooms() {
        final int ROW_INDEX = 3;

        int columnCount = gpMain.getColumnCount() - 1;
        counter = 1;

        while(counter < columnCount) {
            _sharedContext.loadedSchool.getClassrooms().forEach(c -> {
                Text tNewClassroom = new Text(c.getName());
                tNewClassroom.setUserData(c);

                gpMain.getChildren().add(tNewClassroom);
                GridPane.setConstraints(tNewClassroom, counter, ROW_INDEX, 1, 1, HPos.CENTER, VPos.CENTER);
                counter ++;
            });
        }

        counter = null;
    }

    public void setThematicAxis() {
        final int COLUMN_INDEX = 0;

        _sharedContext.selectedLesson.getSubUnits().forEach(ta -> {
            gpMain.getRowConstraints().add(new RowConstraints());

            Text tNewThematicAxis = new Text(ta.getName());
            tNewThematicAxis.setUserData(ta);

            gpMain.getChildren().add(tNewThematicAxis);
            GridPane.setConstraints(tNewThematicAxis, COLUMN_INDEX, gpMain.getRowCount(), 1, 1, HPos.CENTER, VPos.CENTER);
        });
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem miAddThematicAxis = new MenuItem("Create a new thematic axis");
        MenuItem miDeleteThematicAxis = new MenuItem("Delete selected thematic axis");
        MenuItem miAddLesson = new MenuItem("Create a new lesson");
        MenuItem miDeleteLesson = new MenuItem("Delete selected lesson");
        MenuItem miAddCoreCompetency = new MenuItem("Create a new core competency");
        MenuItem miDeleteCoreCompetency = new MenuItem("Delete selected core competency");

        SeparatorMenuItem smiSeparator = new SeparatorMenuItem();

        miAddThematicAxis.setOnAction(onAddThematicAxis);
        miAddLesson.setOnAction(onAddLesson);
        miAddCoreCompetency.setOnAction(onAddCoreCompetency);

        miDeleteThematicAxis.setOnAction(onDeleteSchoolUnit);
        miDeleteLesson.setOnAction(onDeleteSchoolUnit);
        miDeleteCoreCompetency.setOnAction(onDeleteSchoolUnit);

        if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof ThematicAxis) {
            contextMenu.getItems().addAll(miAddThematicAxis, miAddCoreCompetency, smiSeparator, miDeleteThematicAxis);
        } else if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof Lesson) {
            contextMenu.getItems().addAll(miAddLesson, miAddThematicAxis, smiSeparator, miDeleteLesson);
        } else if (twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue() instanceof CoreCompetency) {
            contextMenu.getItems().addAll(miAddCoreCompetency, smiSeparator, miDeleteCoreCompetency);
        }

        return contextMenu;
    }

    @FXML
    void onClose() {
        Context.getInstance().primaryStage.close();
    }
    
    @FXML
    public void onOpenSettings() {
        _sharedContext.showSettingsView();
    }

    private void collapseTreeView(TreeItem<?> ti){
        if(ti.getValue() != _sharedContext.selectedLesson && !ti.isLeaf()){
            if (ti.getValue() instanceof Lesson) {
                ti.setExpanded(false);
            }

            for(TreeItem<?> child:ti.getChildren()){
                collapseTreeView(child);
            }
        }
    }

    private void onTreeItemStateChanged(TreeItem<Object> tiLesson, boolean newValue) {
        if (newValue) {
            _sharedContext.selectedLesson = (Lesson) tiLesson.getValue();
        }
        collapseTreeView(twSchoolYearPlan.getRoot());
    }
}