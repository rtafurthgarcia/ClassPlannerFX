package ch.hftm.util;

import java.util.ArrayList;
import java.util.List;

import ch.hftm.component.FileViewerContainer;
import ch.hftm.model.Classroom;
import ch.hftm.model.SchoolUnit;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class GridPaneHelper {
    static final int COLUMN_STARTING_INDEX = 0;

    static final String CSS_CLASS_FIRST = "header-first";
    static final String CSS_CLASS = "header"; 
    static final String CSS_CLASS_RIGHT = "header-right"; 
    static final String CSS_CLASS_THEMATIC_AXIS = "thematic-axis";
    static final String CSS_CLASS_THEMATIC_AXIS_FIRST = "thematic-axis-first";

    public record ComponentsRow(Label relatedLabel, ArrayList<FileViewerContainer> containers, ThematicAxis thematicAxis) { }
    public record ComponentsColumn(SchoolYearQuarter quarter, Classroom classroom) {};

    public static ComponentsRow addGridRow(GridPane pane, ThematicAxis thematicAxis, ArrayList<ComponentsColumn> columns) {
        final int COLUMN_MAX = pane.getColumnCount() - 1;

        RowConstraints rc =  new RowConstraints();
        rc.setFillHeight(true);
        rc.setVgrow(Priority.SOMETIMES);
        pane.getRowConstraints().add(rc);
            
        Label lThematicAxis = new Label();
        // after the first 4 headers
        if (pane.getRowCount() == 5) {
            lThematicAxis.getStyleClass().add(CSS_CLASS_THEMATIC_AXIS_FIRST);
        } else {
            lThematicAxis.getStyleClass().add(CSS_CLASS_THEMATIC_AXIS);
        }
        lThematicAxis.textProperty().bindBidirectional(thematicAxis.nameProperty());
        lThematicAxis.setMaxWidth(Double.MAX_VALUE);
        lThematicAxis.setMaxHeight(Double.MAX_VALUE);
        lThematicAxis.setMinWidth(100);
        lThematicAxis.setWrapText(true);

        pane.getChildren().add(lThematicAxis);
        GridPane.setConstraints(lThematicAxis, COLUMN_STARTING_INDEX, pane.getRowCount() - 1, 1, 1, HPos.CENTER, VPos.CENTER);
        
        ComponentsRow componentsRow = new ComponentsRow(lThematicAxis, new ArrayList<>(), thematicAxis);
        
        for (int i = COLUMN_STARTING_INDEX + 1; i < COLUMN_MAX + 1; i++) {
            FileViewerContainer container = new FileViewerContainer();
            
            container.setClassroom((columns.get(i - 1).classroom()));
            container.setQuarter(columns.get(i - 1).quarter());
            container.setThematicAxis(thematicAxis);
           
            pane.getChildren().add(container);
            GridPane.setConstraints(container, i, pane.getRowCount() - 1, 1, 1, HPos.CENTER, VPos.CENTER);

            componentsRow.containers.add(container);
        }

        pane.getChildren().get(pane.getChildren().size() - 1).getStyleClass().remove(FileViewerContainer.CSS_CLASS);
        pane.getChildren().get(pane.getChildren().size() - 1).getStyleClass().add(FileViewerContainer.CSS_CLASS_RIGHT);

        return componentsRow;
    }

    public static void removeGridRow(GridPane pane, ThematicAxis thematicAxis, ArrayList<ComponentsRow> list) {
        ComponentsRow row = list.stream()
                            .filter(t -> t.thematicAxis().equals(thematicAxis))
                            .findFirst()
                            .get();

        thematicAxis.getSubUnits().clear();
        pane.getChildren().removeAll(row.containers());
        pane.getChildren().remove(row.relatedLabel());
        list.remove(row);
    } 

    public static void addGridHeaderRow(GridPane pane, List<?> list, int columnSpan, int columnStartOffset) {
        pane.getRowConstraints().add(new RowConstraints(30));
        addGridHeaderRow(pane, list, columnSpan, columnStartOffset, pane.getRowCount() -1);
    }

    public static void addGridHeaderRow(GridPane pane, List<?> list, int columnSpan, int columnStartOffset, int rowIndex) {
        for(int i = 0; i < list.size(); i++) {
            Label lHeader = new Label(list.get(i).toString());

            if (list.get(i) instanceof Classroom) {
                lHeader.textProperty().bind(((Classroom) list.get(i)).nameProperty());
            }

            if (pane.getRowCount() == 1) {
                lHeader.getStyleClass().add(CSS_CLASS_FIRST);
            } else if (i == list.size() - 1) {
                lHeader.getStyleClass().add(CSS_CLASS);
            } else {
                lHeader.getStyleClass().add(CSS_CLASS);
            }

            lHeader.setMaxWidth(Double.MAX_VALUE);
            lHeader.setMaxHeight(Double.MAX_VALUE);    
            pane.getChildren().add(lHeader);
    
            GridPane.setConstraints(lHeader, pane.getColumnCount() / list.size() * i + columnStartOffset, rowIndex, columnSpan, 1, HPos.CENTER, VPos.CENTER);
        }
    }

}
