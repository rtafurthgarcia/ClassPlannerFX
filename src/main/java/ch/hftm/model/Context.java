package ch.hftm.model;

import java.io.IOException;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import ch.hftm.ClassPlannerFX;
import ch.hftm.controller.MainViewController;
import ch.hftm.controller.SettingsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Context {
    private final static Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }

    public Stage primaryStage;

    public String schoolName;

    public Set<Classroom> classrooms;
    public Set<Lesson> lessons;
    public Set<SchoolYear> schoolYears;
    public Set<SchoolYearQuarter> schoolYearQuarters;
    public Set<ThematicAxis> thematicAxises;
    public Set<CoreCompetency> coreCompetencies;

    public Lesson selectedLesson;
    public SchoolYear selectedSchoolYear;

    public DateTimeFormatter dateFormatUsed;

    public void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClassPlannerFX.class.getResource("view/MainView.fxml"));

            // Show the scene containing the root layout.
            Scene scene = new Scene(loader.load());
            instance.primaryStage.setScene(scene);
            instance.primaryStage.show();

            MainViewController controller = loader.getController();
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
