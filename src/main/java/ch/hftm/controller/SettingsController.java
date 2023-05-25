package ch.hftm.controller;

import java.io.IOException;
import java.time.LocalDate;

import ch.hftm.ClassPlannerFX;
import ch.hftm.model.Classroom;
import ch.hftm.model.Context;
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
    private GridPane gpYears;

    @FXML
    private GridPane gpClassrooms;

    private TextField tfAuthor;
    private TextField tfSchoolName;
    
    private TextArea taComment;

    private ListView<Classroom> lvClassrooms;
    private ListView<SchoolYear> lvYears;

    private Button bAddYear;
    private Button bDeleteYear;
    private Button bAddClassroom;
    private Button bDeleteClassroom;

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
        Scene scene = new Scene(loader.load(), 640, 480);
        
        Stage settingsStage = new Stage();
        settingsStage.setResizable(false);
        
        settingsStage.setTitle("Planning settings");
        settingsStage.setScene(scene);
        settingsStage.show();
        
        loader.getController();

        return settingsStage;
    }

    private void bindControlsAndComponents() {
        ToolBar tbYears = (ToolBar) gpYears.lookup("#tbYears");
        ToolBar tbClassrooms = (ToolBar) gpClassrooms.lookup("#tbClassrooms");

        tfAuthor = (TextField) gpGeneral.lookup("#tfAuthor");
        tfSchoolName = (TextField) gpGeneral.lookup("#tfSchoolName");

        taComment = (TextArea) gpGeneral.lookup("#taComment");

        lvClassrooms = (ListView<Classroom>) gpClassrooms.lookup("#lvClassrooms");
        lvYears = (ListView<SchoolYear>) gpYears.lookup("#lvYears");

        lvYears.setEditable(true);
        lvYears.setCellFactory(param -> new TextFieldListCell<>(new SchoolYearConverter()));

        lvClassrooms.setEditable(true);
        lvClassrooms.setCellFactory(param -> new TextFieldListCell<>(new ClassroomConverter()));
        //lvClassrooms.setCellFactory();

        dpBegin1 = (DatePicker) gpYears.lookup("#dpBegin1");
        dpEnd1 = (DatePicker) gpYears.lookup("#dpEnd1");
        dpBegin2 = (DatePicker) gpYears.lookup("#dpBegin2");
        dpEnd2 = (DatePicker) gpYears.lookup("#dpEnd2");
        dpBegin3 = (DatePicker) gpYears.lookup("#dpBegin3");
        dpEnd3 = (DatePicker) gpYears.lookup("#dpEnd3");
        dpBegin4 = (DatePicker) gpYears.lookup("#dpBegin4");
        dpEnd4 = (DatePicker) gpYears.lookup("#dpEnd4");

        bAddYear = (Button) tbYears.getItems().get(0);
        bDeleteYear = (Button) tbYears.getItems().get(1);

        bAddClassroom = (Button) tbClassrooms.getItems().get(0);
        bDeleteClassroom = (Button) tbClassrooms.getItems().get(1);

        cbArchived = (CheckBox) gpYears.lookup("#cbArchived");

        bAddYear.setOnMouseClicked(event -> onAddYear());
        bDeleteYear.setOnMouseClicked(event -> onDeleteYear());
        bAddClassroom.setOnMouseClicked(event -> onAddClassroom());
        bDeleteClassroom.setOnMouseClicked(event -> onDeleteClassroom());
    }

    private void loadValues() {
        tfAuthor.textProperty().bindBidirectional(sharedContext.getLoadedSchool().authorProperty());
        tfSchoolName.textProperty().bindBidirectional(sharedContext.getLoadedSchool().nameProperty());
        taComment.textProperty().bindBidirectional(sharedContext.getLoadedSchool().commentProperty());
        lvClassrooms.setItems(sharedContext.getLoadedSchool().getClassrooms());
        lvYears.setItems(sharedContext.getLoadedSchool().getSubUnits());

        lvYears.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadYearsValues(oldValue, newValue));
        lvYears.getSelectionModel().select(sharedContext.getSelectedSchoolYear());
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

        SchoolYearQuarter q1 = selectedYear.getQuarter(1);
        SchoolYearQuarter q2 = selectedYear.getQuarter(2);
        SchoolYearQuarter q3 = selectedYear.getQuarter(3);
        SchoolYearQuarter q4 = selectedYear.getQuarter(4);

        dpBegin1.valueProperty().bindBidirectional(q1.startWeekProperty());
        dpEnd1.valueProperty().bindBidirectional(q1.endWeekProperty());

        dpBegin2.valueProperty().bindBidirectional(q2.startWeekProperty());
        dpEnd2.valueProperty().bindBidirectional(q2.endWeekProperty());

        dpBegin3.valueProperty().bindBidirectional(q3.startWeekProperty());
        dpEnd3.valueProperty().bindBidirectional(q3.endWeekProperty());

        dpBegin4.valueProperty().bindBidirectional(q4.startWeekProperty());
        dpEnd4.valueProperty().bindBidirectional(q4.endWeekProperty());

        cbArchived.selectedProperty().bindBidirectional(selectedYear.archivedProperty());
    }

    @FXML
    public void initialize(){
        bindControlsAndComponents();
        loadValues();
    }

    @FXML
    void onClose() {
        sharedContext.getLoadedSchool().setAuthor(tfAuthor.getText());
        sharedContext.getLoadedSchool().setName(tfSchoolName.getText());

        sharedContext.getSecondaryStage().close();
    }

    void onAddYear() {
        String newYear = String.format("%d-%d", LocalDate.now().getYear(), LocalDate.now().plusYears(1).getYear()); 

        lvYears.getItems().add(new SchoolYear(newYear));
        lvYears.getSelectionModel().selectLast();
        lvYears.edit(lvYears.getSelectionModel().getSelectedIndex());
        
    }

    void onDeleteYear() {
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
    }

    void onAddClassroom() {
        lvClassrooms.getItems().add(new Classroom("new classroom"));
        lvClassrooms.getSelectionModel().selectLast();
        lvClassrooms.edit(lvClassrooms.getSelectionModel().getSelectedIndex());
    }

    void onDeleteClassroom() {
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
