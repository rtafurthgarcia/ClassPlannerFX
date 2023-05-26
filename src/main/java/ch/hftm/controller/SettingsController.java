package ch.hftm.controller;

import java.io.IOException;
import java.time.LocalDate;

import ch.hftm.ClassPlannerFX;
import ch.hftm.model.Classroom;
import ch.hftm.model.Context;
import ch.hftm.model.School;
import ch.hftm.model.SchoolYear;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.util.ListViewConverter;
import ch.hftm.util.ListViewConverter.ClassroomConverter;
import ch.hftm.util.ListViewConverter.SchoolYearConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SettingsController {
    private Context sharedContext = Context.getInstance();

    @FXML
    private GridPane gpGeneral;

    @FXML
    private GridPane gpYearsClassrooms;

    private TextField tfAuthor;
    private TextField tfSchoolName;
    
    private TextArea taComment;

    private ListView<Classroom> lvClassrooms;
    private ListView<SchoolYear> lvYears;

    private ListView focusedListView;

    private Button bAddItem;
    private Button bDeleteItem;

    private DatePicker dpBegin1;
    private DatePicker dpEnd1;
    private DatePicker dpBegin2;
    private DatePicker dpEnd2;
    private DatePicker dpBegin3;
    private DatePicker dpEnd3;
    private DatePicker dpBegin4;
    private DatePicker dpEnd4;
    
    private CheckBox cbArchived;

    public static Stage showSettingsView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClassPlannerFX.class.getResource("view/SettingsView.fxml"));
        Scene scene = new Scene(loader.load(), 640, 540);
        
        Stage settingsStage = new Stage();
        settingsStage.setResizable(false);
        
        settingsStage.setTitle("Planning settings");
        settingsStage.setScene(scene);
        settingsStage.show();
        
        scene.focusOwnerProperty().addListener((observable, oldValue, newValue) -> ((SettingsController) loader.getController()).setFocusedListView(newValue));

        return settingsStage;
    }

    private void setControlsAndComponents() {     
        ToolBar tbYears = (ToolBar) gpYearsClassrooms.lookup("#tbYears");
        
        tfAuthor = (TextField) gpGeneral.lookup("#tfAuthor");
        tfSchoolName = (TextField) gpGeneral.lookup("#tfSchoolName");

        taComment = (TextArea) gpGeneral.lookup("#taComment");

        lvClassrooms = (ListView<Classroom>) gpYearsClassrooms.lookup("#lvClassrooms");
        lvYears = (ListView<SchoolYear>) gpYearsClassrooms.lookup("#lvYears");

        lvYears.setEditable(true);
        lvYears.setCellFactory(param -> new TextFieldListCell<>(new SchoolYearConverter()));
        lvYears.setFocusTraversable(true);

        lvClassrooms.setEditable(true);
        lvClassrooms.setCellFactory(param -> new TextFieldListCell<>(new ClassroomConverter()));
        lvClassrooms.setFocusTraversable(true);
        //lvClassrooms.setCellFactory();

        dpBegin1 = (DatePicker) gpYearsClassrooms.lookup("#dpBegin1");
        dpEnd1 = (DatePicker) gpYearsClassrooms.lookup("#dpEnd1");
        dpBegin2 = (DatePicker) gpYearsClassrooms.lookup("#dpBegin2");
        dpEnd2 = (DatePicker) gpYearsClassrooms.lookup("#dpEnd2");
        dpBegin3 = (DatePicker) gpYearsClassrooms.lookup("#dpBegin3");
        dpEnd3 = (DatePicker) gpYearsClassrooms.lookup("#dpEnd3");
        dpBegin4 = (DatePicker) gpYearsClassrooms.lookup("#dpBegin4");
        dpEnd4 = (DatePicker) gpYearsClassrooms.lookup("#dpEnd4");

        bAddItem = (Button) tbYears.getItems().get(0);
        bDeleteItem = (Button) tbYears.getItems().get(1);

        cbArchived = (CheckBox) gpYearsClassrooms.lookup("#cbArchived");

        bAddItem.setOnMouseClicked(event -> onAddItem());
        bDeleteItem.setOnMouseClicked(event -> onDeleteItem());
    }

    private void loadValues() {
        tfAuthor.textProperty().bindBidirectional(sharedContext.getLoadedSchool().authorProperty());
        tfSchoolName.textProperty().bindBidirectional(sharedContext.getLoadedSchool().nameProperty());
        taComment.textProperty().bindBidirectional(sharedContext.getLoadedSchool().commentProperty());

        lvYears.setItems(sharedContext.getLoadedSchool().getSubUnits());

        lvYears.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadYearsValues(oldValue, newValue));
        lvYears.getSelectionModel().select(sharedContext.getSelectedSchoolYear());

        cbArchived.selectedProperty().addListener((observable, oldValue, newValue) -> setQuartersEditable(newValue));
    }

    private void loadYearsValues(SchoolYear previouslySelectedYear, SchoolYear selectedYear) {
        if (previouslySelectedYear != null) {
            SchoolYearQuarter q1 = previouslySelectedYear.getQuarter(1);
            SchoolYearQuarter q2 = previouslySelectedYear.getQuarter(2);
            SchoolYearQuarter q3 = previouslySelectedYear.getQuarter(3);
            SchoolYearQuarter q4 = previouslySelectedYear.getQuarter(4);

            dpBegin1.valueProperty().unbindBidirectional(q1.startWeekProperty());
            dpEnd1.valueProperty().unbindBidirectional(q1.endWeekProperty());
    
            dpBegin2.valueProperty().unbindBidirectional(q2.startWeekProperty());
            dpEnd2.valueProperty().unbindBidirectional(q2.endWeekProperty());
    
            dpBegin3.valueProperty().unbindBidirectional(q3.startWeekProperty());
            dpEnd3.valueProperty().unbindBidirectional(q3.endWeekProperty());
    
            dpBegin4.valueProperty().unbindBidirectional(q4.startWeekProperty());
            dpEnd4.valueProperty().unbindBidirectional(q4.endWeekProperty());

            cbArchived.selectedProperty().unbindBidirectional(previouslySelectedYear.archivedProperty());
        }
        
        LocalDate lastYearsDate = LocalDate.now();
        if (lvYears.getItems().size() > 1) {
            lastYearsDate = lvYears.getItems().get(lvYears.getItems().size() -2).getEndDay();
        }

        SchoolYearQuarter q1 = new SchoolYearQuarter(1, lastYearsDate, lastYearsDate.plusMonths(3));
        SchoolYearQuarter q2 = new SchoolYearQuarter(2, lastYearsDate.plusMonths(3).plusDays(1), lastYearsDate.plusMonths(6));
        SchoolYearQuarter q3 = new SchoolYearQuarter(3, lastYearsDate.plusMonths(6).plusDays(1), lastYearsDate.plusMonths(9));
        SchoolYearQuarter q4 = new SchoolYearQuarter(3, lastYearsDate.plusMonths(9).plusDays(1), lastYearsDate.plusYears(1));

        if (! selectedYear.getQuarters().isEmpty()) {
            q1 = selectedYear.getQuarter(1);
            q2 = selectedYear.getQuarter(2);
            q3 = selectedYear.getQuarter(3);
            q4 = selectedYear.getQuarter(4);
        }

        dpBegin1.valueProperty().bindBidirectional(q1.startWeekProperty());
        dpEnd1.valueProperty().bindBidirectional(q1.endWeekProperty());

        dpBegin2.valueProperty().bindBidirectional(q2.startWeekProperty());
        dpEnd2.valueProperty().bindBidirectional(q2.endWeekProperty());

        dpBegin3.valueProperty().bindBidirectional(q3.startWeekProperty());
        dpEnd3.valueProperty().bindBidirectional(q3.endWeekProperty());

        dpBegin4.valueProperty().bindBidirectional(q4.startWeekProperty());
        dpEnd4.valueProperty().bindBidirectional(q4.endWeekProperty());

        cbArchived.selectedProperty().bindBidirectional(selectedYear.archivedProperty());

        lvClassrooms.setItems(selectedYear.getClassrooms());

        lvYears.onEditCommitProperty().addListener((observable, oldValue, newValue) -> );
    }

    private void onVerifyYear(SchoolYear schoolYear) {
        //if lvYears.getItems().contains(schoolYear)
    }

    private void setQuartersEditable(boolean isEditable) {
        dpBegin1.setDisable(isEditable);
        dpEnd1.setDisable(isEditable);

        dpBegin2.setDisable(isEditable);
        dpEnd2.setDisable(isEditable);

        dpBegin3.setDisable(isEditable);
        dpEnd3.setDisable(isEditable);

        dpBegin4.setDisable(isEditable);
        dpEnd4.setDisable(isEditable);
    }

    public void setFocusedListView(Object object) {
        if (object instanceof ListView) {
            focusedListView = (ListView) object;
        }
    }

    void onAddItem() {
        if (focusedListView == lvYears) {
            LocalDate lastYear = LocalDate.now();
            if (! lvYears.getItems().isEmpty()) {
                lastYear = lvYears.getItems().get(lvYears.getItems().sorted((y1, y2) -> y1.getStartDay().compareTo(y2.getStartDay())).size()-1).getEndDay();
            }

            String newYear = String.format("%d-%d", lastYear.getYear(), lastYear.plusYears(1).getYear()); 
    
            lvYears.getItems().add(new SchoolYear(newYear));
            lvYears.getSelectionModel().selectLast();
            lvYears.edit(lvYears.getSelectionModel().getSelectedIndex());
        } else if (focusedListView == lvClassrooms) {
            lvClassrooms.getItems().add(new Classroom("new classroom"));
            lvClassrooms.getSelectionModel().selectLast();
            lvClassrooms.edit(lvClassrooms.getSelectionModel().getSelectedIndex());
        }
        
    }

    void onDeleteItem() {
        if (focusedListView == lvYears) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Delete Item");
            alert.setContentText("Are you sure you want to delete this year and its related data?");

            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeYes) {
                    lvYears.getItems().remove(lvYears.getSelectionModel().getSelectedItem());
                }
            });
        } else if (focusedListView == lvClassrooms) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Delete Item");
            alert.setContentText("Are you sure you want to delete this classroom and its related data?");
    
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
    
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
    
            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeYes) {
                    lvClassrooms.getItems().remove(lvClassrooms.getSelectionModel().getSelectedItem());
                }
            });
        }
    }

    @FXML
    public void initialize(){
        setControlsAndComponents();
        loadValues();
    }

    @FXML
    void onClose() {
        sharedContext.getSecondaryStage().close();
    }
}
