package ch.hftm.controller;

import java.io.IOException;

import ch.hftm.ClassPlannerFX;
import ch.hftm.model.Classroom;
import ch.hftm.model.Context;
import ch.hftm.model.SchoolYear;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SettingsController {
    private Context sharedContext = Context.getInstance();

    @FXML
    private GridPane gpGeneral;

    @FXML
    private GridPane gpYears;

    @FXML
    private BorderPane bpClassrooms;

    private TextField tfAuthor;
    private TextField tfSchoolName;

    private ListView<Classroom> lvClassrooms;
    private ListView<SchoolYear> lvYears;

    public static Stage showSettingsView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClassPlannerFX.class.getResource("view/SettingsView.fxml"));
        Scene scene = new Scene(loader.load(), 600, 480);
        
        Stage settingsStage = new Stage();
        settingsStage.setResizable(false);
        
        settingsStage.setTitle("Planning settings");
        settingsStage.setScene(scene);
        settingsStage.show();
        
        loader.getController();

        return settingsStage;
    }

    private void bindControlsAndComponents() {
        tfAuthor = (TextField) gpGeneral.lookup("#tfAuthor");
        tfSchoolName = (TextField) gpGeneral.lookup("#tfSchoolName");
        lvClassrooms = (ListView<Classroom>) bpClassrooms.lookup("#lvClassrooms");
        lvYears = (ListView<SchoolYear>) gpYears.lookup("#lvYears");
    }

    private void loadValues() {
        tfAuthor.setText(sharedContext.getLoadedSchool().getAuthor());
        tfSchoolName.setText(sharedContext.getLoadedSchool().getName());
        lvClassrooms.setItems(sharedContext.getLoadedSchool().getClassrooms());
        lvYears.setItems(sharedContext.getLoadedSchool().getSubUnits());
    }

    @FXML
    public void initialize(){
        bindControlsAndComponents();
        loadValues();
    }

    @FXML
    void onSave() {
        sharedContext.getLoadedSchool().setAuthor(tfAuthor.getText());
        sharedContext.getLoadedSchool().setName(tfSchoolName.getText());

        sharedContext.getSecondaryStage().close();
    }

    @FXML
    void onCancel() {
        Context.getInstance().getSecondaryStage().close();
    }
}
