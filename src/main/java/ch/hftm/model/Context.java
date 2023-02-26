package ch.hftm.model;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.hildan.fxgson.FxGson;

import com.google.gson.Gson;

import ch.hftm.ClassPlannerFX;
import ch.hftm.controller.MainViewController;
import ch.hftm.controller.SettingsController;
import ch.hftm.util.SerializationHelper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Context {
    private final static Context instance = new Context();

    private MainViewController mainController;

    private Stage primaryStage;
    private School loadedSchool;
    private DateTimeFormatter dateFormatUsed;

    private ObjectProperty<Lesson> selectedLesson = new SimpleObjectProperty<>();
    private ObjectProperty<SchoolYear> selectedSchoolYear = new SimpleObjectProperty<>();

    private StringProperty saveFilePath = new SimpleStringProperty("");

    private Gson serializer = FxGson.fullBuilder()
        .registerTypeAdapter(File.class, new SerializationHelper.FileAdapter())
        .registerTypeAdapter(LocalDate.class, new SerializationHelper.LocalDateAdapter())
        .registerTypeAdapter(DateTimeFormatter.class, new SerializationHelper.DateTimeFormatterAdapter())
        .setPrettyPrinting()
        .create();

    private LogManager logManager = LogManager.getLogManager();
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public LogManager getLogManager() {
        return logManager;
    }

    public Gson getSerializer() {
        return serializer;
    }

    public String getSaveFilePath() {
        return saveFilePath.get();
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath.set(saveFilePath);
    }

    public StringProperty saveFilePathProperty(){
        return saveFilePath;
    }

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

    public MainViewController getMainController() {
        return mainController;
    }

    public void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClassPlannerFX.class.getResource("view/MainView.fxml"));

            // Show the scene containing the root layout.
            Scene scene = new Scene(loader.load());
            instance.primaryStage.setScene(scene);
            instance.primaryStage.show();

            mainController = loader.getController();
        } catch (IOException exception) {
            logger.log(Level.SEVERE, exception.toString());
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
