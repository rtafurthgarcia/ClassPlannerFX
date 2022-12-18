package ch.hftm.controller;

import java.io.IOException;

import ch.hftm.ClassPlannerFX;
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

    private ClassPlannerFX _app;  
    private Stage _stage;

    public void setApp(ClassPlannerFX app) throws IOException {
        this._app = app;

        this.pGeneral = FXMLLoader.load(ClassPlannerFX.class.getResource("view/GeneralView.fxml"));
        this.pQuarters = FXMLLoader.load(ClassPlannerFX.class.getResource("view/QuartersView.fxml"));
        this.pClassrooms = FXMLLoader.load(ClassPlannerFX.class.getResource("view/ClassroomsView.fxml"));
    }

    public void setStage(Stage stage) {
        this._stage = stage;
    }


    @FXML
    public void initialize(){

    }

    @FXML
    void onClose() {
        this._app.getPrimaryStage().close();
    }
}
