package ch.hftm;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import ch.hftm.controller.MainViewController;
import ch.hftm.controller.SettingsController;
import ch.hftm.model.Classroom;
import ch.hftm.model.Context;
import ch.hftm.model.Lesson;
import ch.hftm.model.SchoolYear;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
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
    public void start(Stage primaryStage) throws ParseException {
        _sharedContext.primaryStage = primaryStage;
        _sharedContext.primaryStage.setTitle("ClassPlannerFX");

        generateDefaultValues();

        _sharedContext.showMainView();
    }

    public void generateDefaultValues() throws ParseException {

        _sharedContext.schoolName = "Berner Primärschule";
        _sharedContext.dateFormatUsed = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		//_sharedContext.dateFormatUsed = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMANY); //DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("fr", "CH"));

		_sharedContext.classrooms = new HashSet<> (Arrays.asList(new Classroom("Classe 711"), new Classroom("Classe 712")));
		_sharedContext.schoolYear = new SchoolYear(LocalDate.parse("02.05.2023", _sharedContext.dateFormatUsed), LocalDate.parse("29.04.2024", _sharedContext.dateFormatUsed));
        _sharedContext.schoolYearQuarters = new HashSet<> (Arrays.asList(
            new SchoolYearQuarter(1, 4, 19), 
            new SchoolYearQuarter(2, 20, 35), 
            new SchoolYearQuarter(3, 36, 51), 
            new SchoolYearQuarter(4, 52, 68)
        ));

        Lesson lessonFrench =  new Lesson("Français", _sharedContext.schoolYear);
        Lesson lessonGeography =  new Lesson("Geographie", _sharedContext.schoolYear);
        Lesson lessonMaths =  new Lesson("Maths", _sharedContext.schoolYear);
        _sharedContext.lessons = new HashSet<>(Arrays.asList(lessonFrench, lessonGeography, lessonMaths));

        _sharedContext.thematicAxises = new HashSet<>(Arrays.asList(
            new ThematicAxis("Vocabulaire 1", 1, lessonFrench, _sharedContext.schoolYear),
            new ThematicAxis("Verbes irréguliers G4", 2, lessonFrench, _sharedContext.schoolYear),
            new ThematicAxis("Poésie", 3, lessonFrench, _sharedContext.schoolYear),
            new ThematicAxis("Océanie", 1, lessonGeography, _sharedContext.schoolYear)
        ));
	}
}