package ch.hftm;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import ch.hftm.model.Classroom;
import ch.hftm.model.Context;
import ch.hftm.model.CoreCompetency;
import ch.hftm.model.Intersection;
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

        primaryStage.setMaximized(true);
        sharedContext.setPrimaryStage(primaryStage);
        sharedContext.getPrimaryStage().setTitle("ClassPlannerFX");

        sharedContext.showMainView();

        try {
            sharedContext.getLogManager()
                    .readConfiguration(ClassPlannerFX.class.getResourceAsStream(("resources/logging.properties")));
            sharedContext.setLogger(Logger.getLogger(ClassPlannerFX.class.getName()));
            sharedContext.getLogger().addHandler(new ConsoleHandler());
            sharedContext.getLogger().addHandler(new FileHandler("ClassPlannerFX.log", 2000, 5));
            sharedContext.getLogManager().addLogger(sharedContext.getLogger());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateDefaultValues() throws ParseException {

        sharedContext.setLoadedSchool(new School("Berner Primärschule"));
        sharedContext.setDateFormatUsed(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        sharedContext.getLoadedSchool().getClassrooms().addAll(new Classroom("Classe 711"),
                new Classroom("Classe 712"));

        sharedContext
                .setSelectedSchoolYear(new SchoolYear(LocalDate.parse("02.05.2023", sharedContext.getDateFormatUsed()),
                        LocalDate.parse("29.04.2024", sharedContext.getDateFormatUsed())));

        sharedContext.getSelectedSchoolYear().getQuarters().addAll(
                new SchoolYearQuarter(1, 4, 19),
                new SchoolYearQuarter(2, 20, 35),
                new SchoolYearQuarter(3, 36, 51),
                new SchoolYearQuarter(4, 52, 68));

        SchoolYear schoolYear2025 = new SchoolYear(LocalDate.parse("02.05.2024", sharedContext.getDateFormatUsed()),
                LocalDate.parse("29.04.2025", sharedContext.getDateFormatUsed()));
        schoolYear2025.getQuarters().addAll(
                new SchoolYearQuarter(1, 4, 19),
                new SchoolYearQuarter(2, 20, 35),
                new SchoolYearQuarter(3, 36, 51),
                new SchoolYearQuarter(4, 52, 68));

        sharedContext.getLoadedSchool().getSubUnits().addAll(sharedContext.getSelectedSchoolYear(), schoolYear2025);

        Lesson lessonFrench = new Lesson("Français");
        Lesson lessonGeography = new Lesson("Geographie");
        Lesson lessonMaths = new Lesson("Maths");
        sharedContext.getSelectedSchoolYear().getSubUnits().addAll(lessonFrench, lessonGeography, lessonMaths);

        sharedContext.setSelectedLesson(lessonFrench);

        ThematicAxis taVocabulaire = new ThematicAxis("Vocabulaire 1");
        taVocabulaire.getSubUnits().add(new CoreCompetency("Verbes en ER, IR").setIntersection(
                new Intersection(
                        sharedContext.getLoadedSchool().getClassrooms().get(0),
                        sharedContext.getSelectedSchoolYear().getQuarters().get(0),
                        taVocabulaire)
                )
        );
        taVocabulaire.getSubUnits().add(new CoreCompetency("Verbes en UIR, DRE").setIntersection(
                new Intersection(
                        sharedContext.getLoadedSchool().getClassrooms().get(1),
                        sharedContext.getSelectedSchoolYear().getQuarters().get(2),
                        taVocabulaire)
                )
        );

        sharedContext.getSelectedLesson().getSubUnits().addAll(
                taVocabulaire,
                new ThematicAxis("Verbes irréguliers G4"),
                new ThematicAxis("Poésie"));

        lessonGeography.getSubUnits().add(new ThematicAxis("Océanie"));
    }
}