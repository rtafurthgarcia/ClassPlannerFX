package ch.hftm;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.hftm.controller.MainViewController;
import ch.hftm.controller.SettingsController;
import ch.hftm.model.Context;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;

/**
 * JavaFX App
 */
public class ClassPlannerFX extends Application {
    private Context _sharedContext = Context.getInstance();

    @Override
    public void start(Stage primaryStage) {
        _sharedContext.primaryStage = primaryStage;
        _sharedContext.primaryStage.setTitle("ClassPlannerFX");

        _sharedContext.showMainView();
    }
}