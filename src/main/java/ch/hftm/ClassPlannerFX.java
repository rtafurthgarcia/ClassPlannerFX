package ch.hftm;

import java.io.IOException;

import ch.hftm.controller.MainViewController;
import ch.hftm.controller.SettingsController;
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

    private Stage _primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this._primaryStage = primaryStage;
        this._primaryStage.setTitle("ClassPlannerFX");

        this.showMainView();
    }

    public void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClassPlannerFX.class.getResource("view/MainView.fxml"));

            // Show the scene containing the root layout.
            Scene scene = new Scene(loader.load());
            _primaryStage.setScene(scene);
            _primaryStage.show();

            MainViewController controller = loader.getController();
            controller.setApp(this);            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSettingsView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClassPlannerFX.class.getResource("view/SettingsView.fxml"));
            Scene scene = new Scene(loader.load(), 480, 480);
            
            Stage settingsView = new Stage();
            settingsView.setResizable(false);
            
            settingsView.setTitle("Planning settings");
            settingsView.setScene(scene);
            settingsView.show();
            
            SettingsController settingsViewController = loader.getController();
            settingsViewController.setStage(settingsView);
            settingsViewController.setApp(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
		return this._primaryStage;
	}
}