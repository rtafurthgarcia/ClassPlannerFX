package ch.hftm.controller;

import org.controlsfx.control.spreadsheet.SpreadsheetView;

import ch.hftm.ClassPlannerFX;
import ch.hftm.model.Context;
import ch.hftm.model.CoreCompetency;
import ch.hftm.model.Lesson;
import ch.hftm.model.School;
import ch.hftm.model.SchoolYear;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
import ch.hftm.util.TextFieldTreeCellImpl;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class MainViewController {

    @FXML
    private SpreadsheetView svMain;

    @FXML
    private GridPane gpMain;

    @FXML
    private TreeView<School> twSchoolYearPlan;

    private Context _sharedContext = Context.getInstance();

    private Integer counter;

    /*private TreeItem<Object> tiSelectedSchoolYear;
    private TreeItem<Object> tiSelectedLesson;
    private TreeItem<Object> tiSelectedThematicAxis;*/

    //private ImageView ivSelect = new ImageView(new Image(ClassPlannerFX.class.getResourceAsStream("resources/arrow-right.png")));

    @FXML
    public void initialize() {
        //_sharedContext.primaryStage.getScene().getStylesheets().add(ClassPlannerFX.class.getResource("@../resources/css/style.css").toExternalForm());
        setGridConstraints();
        setSemestres();
        setQuarters();
        setQuartersWithWeeks();
        setClassrooms();
        setThematicAxis();

        //loadTreeView();

        //twSchoolYearPlan.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> onNewlySelectedTreeItem(newValue));
    }  

    /*void loadTreeView() {
        TreeItem<Object> tiSchoolName = new TreeItem<>(_sharedContext.schoolName);
        tiSchoolName.setExpanded(true);

        _sharedContext.schoolYears.forEach(sy -> {
            TreeItem<Object> tiSchoolYear = new TreeItem<>();
            tiSchoolYear.setExpanded(true);
            tiSchoolYear.setValue(sy);

            if (sy.equals(_sharedContext.selectedSchoolYear)) {
                tiSelectedSchoolYear = tiSchoolYear;
            }
            tiSchoolName.getChildren().add(tiSchoolYear);

            _sharedContext.lessons.stream()
                .filter(l -> l.getSchoolYear().equals(sy))
                .forEach(le -> {
                    TreeItem<Object> tiLesson = new TreeItem<>(le);
                    tiLesson.setExpanded(true);
                    tiLesson.expandedProperty().addListener((observable, oldValue, newValue) -> onTreeItemStateChanged(tiLesson, newValue));

                    if (le.equals(_sharedContext.selectedLesson)) {
                        tiSelectedLesson = tiLesson;
                    }

                    _sharedContext.thematicAxises.stream()
                        .filter(ta -> ta.getSchoolYear().equals(sy) && ta.getLesson().equals(le))
                        .forEach(ta -> {
                            tiLesson.getChildren().add(new TreeItem<>(ta));
                        });

                    tiSchoolYear.getChildren().add(tiLesson);
                });
        });

        twSchoolYearPlan.setRoot(tiSchoolName);
        twSchoolYearPlan.setEditable(true);
        twSchoolYearPlan.setOnContextMenuRequested(event -> twSchoolYearPlan.setContextMenu(createContextMenu()));
        twSchoolYearPlan.setCellFactory(tw -> 
            new TextFieldTreeCellImpl()
        );
    }*/

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

        /*_sharedContext.loadedSchool.getSchoolYears().forEach(sy -> {
            sy.get
        });*/

        _sharedContext.selectedLesson.getLessonsAxis().forEach(ta -> {
            gpMain.getRowConstraints().add(new RowConstraints());

            Text tNewThematicAxis = new Text(ta.getName());
            tNewThematicAxis.setUserData(ta);

            gpMain.getChildren().add(tNewThematicAxis);
            GridPane.setConstraints(tNewThematicAxis, COLUMN_INDEX, gpMain.getRowCount(), 1, 1, HPos.CENTER, VPos.CENTER);
        });
    }

    /*private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        Object selectedObject = twSchoolYearPlan.getSelectionModel().getSelectedItem().getValue();

        if (selectedObject instanceof ThematicAxis) {
            MenuItem miThematicAxis = new MenuItem("Create a new thematic axis");
            miThematicAxis.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    ThematicAxis thematicAxis = new ThematicAxis("new thematic axis", tiSelectedLesson.getChildren().size(), _sharedContext.selectedLesson, _sharedContext.selectedSchoolYear);                   
                    twSchoolYearPlan.getSelectionModel().getSelectedItem().getParent().getChildren().add(new TreeItem<>(thematicAxis));
                    _sharedContext.thematicAxises.add(thematicAxis);
                    twSchoolYearPlan.getSelectionModel().selectLast();         
                }
            });

            contextMenu.getItems().addAll(miThematicAxis);
        } else if (selectedObject instanceof Lesson) {
            MenuItem miLesson = new MenuItem("Create a new lesson");
            miLesson.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {               
                    Lesson newLesson = new Lesson("new lesson", _sharedContext.selectedSchoolYear);
                    twSchoolYearPlan.getSelectionModel().getSelectedItem().getParent().getChildren().add(new TreeItem<>(newLesson));
                    _sharedContext.lessons.add(newLesson);
                    twSchoolYearPlan.getSelectionModel().selectLast();
                }
            });

            contextMenu.getItems().addAll(miLesson);
        } else if (selectedObject instanceof CoreCompetency) {
            MenuItem miCoreCompetency = new MenuItem("Create a new core competency");
            miCoreCompetency.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {             
                    CoreCompetency newCoreCompetency = new CoreCompetency().setName("new core competency").setDescription("");
                    twSchoolYearPlan.getSelectionModel().getSelectedItem().getParent().getChildren().add(new TreeItem<>(newCoreCompetency));
                    _sharedContext.coreCompetencies.add(newCoreCompetency);
                    twSchoolYearPlan.getSelectionModel().selectLast();
                }
            });
        }

        return contextMenu;
    }*/

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
                //ti.setGraphic(null);
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