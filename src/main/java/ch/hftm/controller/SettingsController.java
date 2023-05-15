package ch.hftm.controller;

import java.io.IOException;

import ch.hftm.ClassPlannerFX;
import ch.hftm.model.Context;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    DatePicker dpStartDay;

    @FXML
    DatePicker dpEndDay;

    @FXML 
    TextField tfAuthor;

    @FXML
    TextField tfSchoolName;

    private Stage stage;

    private Context sharedContext = Context.getInstance();

    public void setApp() throws IOException {
        //this.pGeneral = FXMLLoader.load(ClassPlannerFX.class.getResource("view/GeneralView.fxml"));
        //this.pQuarters = FXMLLoader.load(ClassPlannerFX.class.getResource("view/QuartersView.fxml"));
        //this.pClassrooms = FXMLLoader.load(ClassPlannerFX.class.getResource("view/ClassroomsView.fxml"));
    }

    public static void showSettingsView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClassPlannerFX.class.getResource("view/SettingsView.fxml"));
        Scene scene = new Scene(loader.load(), 480, 480);
        
        Stage settingsStage = new Stage();
        settingsStage.setResizable(false);
        
        settingsStage.setTitle("Planning settings");
        settingsStage.setScene(scene);
        settingsStage.show();
        
        SettingsController settingsViewController = loader.getController();
        settingsViewController.setStage(settingsStage);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    public void initialize(){
        tfAuthor.setText("lmao");
    }

    @FXML
    void onClose() {
        Context.getInstance().getPrimaryStage().close();
    }
}
