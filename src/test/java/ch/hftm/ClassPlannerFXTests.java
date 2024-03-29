package ch.hftm;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import ch.hftm.controller.MainViewController;
import ch.hftm.model.Classroom;
import ch.hftm.model.Context;
import ch.hftm.model.CoreCompetency;
import ch.hftm.model.Intersection;
import ch.hftm.model.Lesson;
import ch.hftm.model.School;
import ch.hftm.model.SchoolYear;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javafx.stage.Stage;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(ApplicationExtension.class)
class ClassPlannerFXTests {

    private Context sharedContext = Context.getInstance();
    MainViewController controller;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
 * @throws IOException
     */
    @Start
	public void start(Stage stage) throws IOException {
        sharedContext.setPrimaryStage(stage);
        
        sharedContext.getPrimaryStage().setTitle("ClassPlannerFX");
        MainViewController.showMainView(stage);

        controller = sharedContext.getMainController();
    }
	
	@BeforeAll
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

	@Test
	public void shouldBeCorrectlySetup() {
		assertTrue(sharedContext.getLoadedSchool().getClassrooms().size() == 2);
        assertTrue(sharedContext.getLoadedSchool().getSubUnits().size() == 2);

	}

    @Test
	public void copyCoreCompetencyToSameThematicAxis(FxRobot robot) {
        //robot.mouseMove(controller.getApTreeView().get);
        
	}


    @Test
    public void isAbleToSaveFile() throws IOException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(School.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(sharedContext.getLoadedSchool(), new File("test.json"));
    }

    @Test
    public void isAbleToReadASaveFile() throws IOException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(School.class);    
        sharedContext.setLoadedSchool((School) context.createUnmarshaller().unmarshal(new File("test.json")));    
        sharedContext.setSelectedSchoolYear(sharedContext.getLoadedSchool().getSubUnits().stream().findFirst().get());
        sharedContext.setSelectedLesson(sharedContext.getLoadedSchool().getSubUnits().stream().findFirst().get().getSubUnits().stream().findFirst().get());

    }
}