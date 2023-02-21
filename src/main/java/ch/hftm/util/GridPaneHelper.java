package ch.hftm.util;

import java.util.ArrayList;
import ch.hftm.component.FileViewerContainer;
import ch.hftm.model.Classroom;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class GridPaneHelper {
    static final int COLUMN_STARTING_INDEX = 0;

    public record ComponentsRow(Text relatedText, ArrayList<FileViewerContainer> containers, ThematicAxis thematicAxis) { }
    public record ComponentsColumn(SchoolYearQuarter quarter, Classroom classroom) {};

    public static ComponentsRow addGridRow(GridPane pane, ThematicAxis thematicAxis, ArrayList<ComponentsColumn> columns) {
        final int COLUMN_MAX = pane.getColumnCount() - 1;

        pane.getRowConstraints().add(new RowConstraints());
            
        Text tNewThematicAxis = new Text();
        tNewThematicAxis.textProperty().bind(thematicAxis.nameProperty());

        pane.getChildren().add(tNewThematicAxis);
        GridPane.setConstraints(tNewThematicAxis, COLUMN_STARTING_INDEX, pane.getRowCount(), 1, 1, HPos.CENTER, VPos.CENTER);
        
        ComponentsRow componentsRow = new ComponentsRow(tNewThematicAxis, new ArrayList<>(), thematicAxis);
        
        for (int i = COLUMN_STARTING_INDEX + 1; i < COLUMN_MAX; i++) {
            FileViewerContainer container = new FileViewerContainer();
            container.setMinSize(120, 180);
            container.setClassroom((columns.get(i - 1).classroom()));
            container.setQuarter(columns.get(i - 1).quarter());
            container.setThematicAxis(thematicAxis);
            
            pane.getChildren().add(container);
            GridPane.setConstraints(container, i, pane.getRowCount() - 1, 1, 1, HPos.CENTER, VPos.CENTER);
            componentsRow.containers.add(container);
        }

        return componentsRow;
    }

    public static void removeGridRow(GridPane pane, ThematicAxis thematicAxis, ArrayList<ComponentsRow> list) {
        ComponentsRow row = list.stream()
                            .filter(t -> t.thematicAxis().equals(thematicAxis))
                            .findFirst()
                            .get();

        pane.getChildren().removeAll(row.containers());
        pane.getChildren().remove(row.relatedText());
        list.remove(row);
    } 
    /*public static ComponentsRow moveThematicAxisAround(GridPane pane, ThematicAxis thematicAxis) {

    }*/
}
