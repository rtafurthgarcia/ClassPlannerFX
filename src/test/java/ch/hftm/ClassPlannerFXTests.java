package ch.hftm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

import ch.hftm.model.Classroom;
import ch.hftm.model.Context;
import ch.hftm.model.Lesson;
import ch.hftm.model.SchoolYear;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(ApplicationExtension.class)
class ClassPlannerFXTests {

    private Context _sharedContext = Context.getInstance();

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
	public void start(Stage stage) {
        _sharedContext.primaryStage = stage;
        
        _sharedContext.primaryStage.setTitle("ClassPlannerFX");

        _sharedContext.showMainView();
    }
	
	@BeforeAll
	public void setup() throws ParseException {
        _sharedContext.schoolName = "Berner Primärschule";
        _sharedContext.dateFormatUsed = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		//_sharedContext.dateFormatUsed = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMANY); //DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("fr", "CH"));

		_sharedContext.classrooms = new HashSet<> (Arrays.asList(new Classroom("Classe 711"), new Classroom("Classe 712")));
		_sharedContext.selectedSchoolYear = new SchoolYear(LocalDate.parse("02.05.2023", _sharedContext.dateFormatUsed), LocalDate.parse("29.04.2024", _sharedContext.dateFormatUsed));
        _sharedContext.schoolYearQuarters = new HashSet<> (Arrays.asList(
            new SchoolYearQuarter(1, 4, 19), 
            new SchoolYearQuarter(2, 20, 35), 
            new SchoolYearQuarter(3, 36, 51), 
            new SchoolYearQuarter(4, 52, 68)
        ));

        Lesson lessonFrench =  new Lesson("Français", _sharedContext.selectedSchoolYear);
        Lesson lessonGeography =  new Lesson("Geographie", _sharedContext.selectedSchoolYear);
        Lesson lessonMaths =  new Lesson("Maths", _sharedContext.selectedSchoolYear);
        _sharedContext.lessons = new HashSet<>(Arrays.asList(lessonFrench, lessonGeography, lessonMaths));
        _sharedContext.selectedLesson = lessonFrench;

        _sharedContext.thematicAxises = new HashSet<>(Arrays.asList(
            new ThematicAxis("Vocabulaire 1", 1, lessonFrench, _sharedContext.selectedSchoolYear),
            new ThematicAxis("Verbes irréguliers G4", 2, lessonFrench, _sharedContext.selectedSchoolYear),
            new ThematicAxis("Poésie", 3, lessonFrench, _sharedContext.selectedSchoolYear),
            new ThematicAxis("Océanie", 1, lessonGeography, _sharedContext.selectedSchoolYear)
        ));
	}

	@Test
	public void shouldBeCorrectlySetup() {
		assertTrue(_sharedContext.schoolYearQuarters.size() == 4);
        assertTrue(_sharedContext.classrooms.size() == 2);
	}

}