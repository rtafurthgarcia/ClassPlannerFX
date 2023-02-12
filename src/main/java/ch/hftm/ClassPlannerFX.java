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
    private Context sharedContext = Context.getInstance();

    @Override
    public void start(Stage primaryStage) throws ParseException {
        generateDefaultValues();

        sharedContext.setPrimaryStage(primaryStage);
        sharedContext.getPrimaryStage().setTitle("ClassPlannerFX");

        sharedContext.showMainView();
    }

    public void generateDefaultValues() throws ParseException {

        sharedContext.setLoadedSchool(new School("Berner Primärschule"));
        sharedContext.setDateFormatUsed(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        sharedContext.getLoadedSchool().getClassrooms().addAll(new Classroom("Classe 711"), new Classroom("Classe 712"));

        sharedContext.setSelectedSchoolYear(new SchoolYear(LocalDate.parse("02.05.2023", sharedContext.getDateFormatUsed()), LocalDate.parse("29.04.2024", sharedContext.getDateFormatUsed())));
        
        sharedContext.getSelectedSchoolYear().getQuarters().addAll(
            new SchoolYearQuarter(1, 4, 19), 
            new SchoolYearQuarter(2, 20, 35), 
            new SchoolYearQuarter(3, 36, 51), 
            new SchoolYearQuarter(4, 52, 68)
        );

        sharedContext.getLoadedSchool().getSubUnits().add(sharedContext.getSelectedSchoolYear());

        Lesson lessonFrench =  new Lesson("Français");
        Lesson lessonGeography =  new Lesson("Geographie");
        Lesson lessonMaths =  new Lesson("Maths");
        sharedContext.getSelectedSchoolYear().getSubUnits().addAll(lessonFrench, lessonGeography, lessonMaths);

        sharedContext.setSelectedLesson(lessonFrench);

        sharedContext.getSelectedLesson().getSubUnits().addAll(
            new ThematicAxis("Vocabulaire 1"),
            new ThematicAxis("Verbes irréguliers G4"),
            new ThematicAxis("Poésie")
        );

        lessonGeography.getSubUnits().add(new ThematicAxis("Océanie"));

        
        /*sharedContext.coreCompetencies = Arrays.asList(
            new CoreCompetency().setName("Maîtrise des verbes en -IR ").setDescription("Les verbes terminés en -IR (comme MOURIR : mour-ant; mour-ons);"),
            new CoreCompetency().setName("Maîtrise des verbes en -OIR").setDescription("Les verbes terminés en -OIR (comme RECEVOIR : recev-ant; recev-ons);"));
        */
	}
}