package ch.hftm.model;

import java.io.IOException;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.hftm.ClassPlannerFX;
import ch.hftm.controller.MainViewController;
import ch.hftm.controller.SettingsController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Context {
    private final static Context instance = new Context();
    
    private Stage primaryStage;
    private School loadedSchool;
    private DateTimeFormatter dateFormatUsed;

    private ObjectProperty<Lesson> selectedLesson = new SimpleObjectProperty<>();
    private ObjectProperty<SchoolYear> selectedSchoolYear = new SimpleObjectProperty<>();

    public ObjectProperty<SchoolYear> selectedSchoolYearProperty() {
        return selectedSchoolYear;
    }

    public void setSelectedSchoolYear(SchoolYear selectedSchoolYear) {
        this.selectedSchoolYear.set(selectedSchoolYear);
    }

    public SchoolYear getSelectedSchoolYear() {
        return this.selectedSchoolYear.get();
    }

    public ObjectProperty<Lesson> selectedLessonProperty() {
        return selectedLesson;
    }

    public void setSelectedLesson(Lesson selectedLesson) {
        this.selectedLesson.set(selectedLesson);
    }

    public Lesson getSelectedLesson() {
        return this.selectedLesson.get();
    }

    public DateTimeFormatter getDateFormatUsed() {
        return dateFormatUsed;
    }

    public void setDateFormatUsed(DateTimeFormatter dateFormatUsed) {
        this.dateFormatUsed = dateFormatUsed;
    }

    public static Context getInstance() {
        return instance;
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public School getLoadedSchool() {
        return loadedSchool;
    }

    public void setLoadedSchool(School loadedSchool) {
        this.loadedSchool = loadedSchool;
    }


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
