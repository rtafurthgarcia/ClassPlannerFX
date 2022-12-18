package ch.hftm.controller;

import ch.hftm.ClassPlannerFX;

import javafx.fxml.FXML;

public class MainViewController {
    private ClassPlannerFX _app;  

    public void setApp(ClassPlannerFX app) {
        this._app = app;
    }

    @FXML
    public void initialize() {
       
    }

    @FXML
    void onClose() {
        this._app.getPrimaryStage().close();
    }

    @FXML
    public void onOpenSettings() {
        this._app.showSettingsView();
    }
}