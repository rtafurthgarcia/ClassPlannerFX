package ch.hftm;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.hftm.model.Classroom;
import ch.hftm.model.Context;
import ch.hftm.model.CoreCompetency;
import ch.hftm.model.Lesson;
import ch.hftm.model.School;
import ch.hftm.model.SchoolYear;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
import javafx.application.Application;
import javafx.stage.Stage;

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

        _sharedContext.loadedSchool = new School("Berner Primärschule");
        _sharedContext.dateFormatUsed = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        _sharedContext.loadedSchool.getClassrooms().addAll(Arrays.asList(new Classroom("Classe 711"), new Classroom("Classe 712")));


        _sharedContext.selectedSchoolYear = new SchoolYear(LocalDate.parse("02.05.2023", _sharedContext.dateFormatUsed), LocalDate.parse("29.04.2024", _sharedContext.dateFormatUsed));
        
        _sharedContext.selectedSchoolYear.getQuarters().addAll(Arrays.asList(
            new SchoolYearQuarter(1, 4, 19), 
            new SchoolYearQuarter(2, 20, 35), 
            new SchoolYearQuarter(3, 36, 51), 
            new SchoolYearQuarter(4, 52, 68)
        ));

        _sharedContext.loadedSchool.getSubUnits().add(_sharedContext.selectedSchoolYear);

        Lesson lessonFrench =  new Lesson("Français");
        Lesson lessonGeography =  new Lesson("Geographie");
        Lesson lessonMaths =  new Lesson("Maths");
        _sharedContext.selectedSchoolYear.getSubUnits().addAll(Arrays.asList(lessonFrench, lessonGeography, lessonMaths));

        _sharedContext.selectedLesson = lessonFrench;

        _sharedContext.selectedLesson.getSubUnits().addAll(Arrays.asList(
            new ThematicAxis("Vocabulaire 1"),
            new ThematicAxis("Verbes irréguliers G4"),
            new ThematicAxis("Poésie")
        ));

        lessonGeography.getSubUnits().add(new ThematicAxis("Océanie"));

        
        /*_sharedContext.coreCompetencies = Arrays.asList(
            new CoreCompetency().setName("Maîtrise des verbes en -IR ").setDescription("Les verbes terminés en -IR (comme MOURIR : mour-ant; mour-ons);"),
            new CoreCompetency().setName("Maîtrise des verbes en -OIR").setDescription("Les verbes terminés en -OIR (comme RECEVOIR : recev-ant; recev-ons);"));
        */
	}
}