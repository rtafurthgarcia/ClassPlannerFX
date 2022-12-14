package ch.hftm.controller;

import ch.hftm.ClassPlannerFX;

import javafx.fxml.FXML;

public class MainViewController {
    private ClassPlannerFX app;  

    public void setApp(ClassPlannerFX app) {
        this.app = app;
    }

    @FXML
    public void initialize() {
       
    }

    @FXML
    void onClose() {
        this.app.getPrimaryStage().close();
    }
}