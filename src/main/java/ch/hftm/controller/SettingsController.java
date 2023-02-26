package ch.hftm.controller;

import java.io.IOException;

import ch.hftm.ClassPlannerFX;
import ch.hftm.model.Context;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    Pane pGeneral;

    @FXML
    Pane pQuarters;

    @FXML
    Pane pClassrooms;

    private Stage stage;

    private Context sharedContext = Context.getInstance();

    public void setApp() throws IOException {
        this.pGeneral = FXMLLoader.load(ClassPlannerFX.class.getResource("view/GeneralView.fxml"));
        this.pQuarters = FXMLLoader.load(ClassPlannerFX.class.getResource("view/QuartersView.fxml"));
        this.pClassrooms = FXMLLoader.load(ClassPlannerFX.class.getResource("view/ClassroomsView.fxml"));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    public void initialize(){

    }

    @FXML
    void onClose() {
        Context.getInstance().getPrimaryStage().close();
    }
}
