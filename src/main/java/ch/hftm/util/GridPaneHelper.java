package ch.hftm.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import ch.hftm.component.FileViewer;
import ch.hftm.component.FileViewerContainer;
import ch.hftm.model.Classroom;
import ch.hftm.model.SchoolYearQuarter;
import ch.hftm.model.ThematicAxis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GridPaneHelper {
    static final int COLUMN_STARTING_INDEX = 0;

    public record ComponentsRow(Text relatedText, ArrayList<FileViewerContainer> boxes, ThematicAxis thematicAxis) { }
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
            container.setOnDragOver(event -> onDragOverContainer(event, container));
            container.setOnDragDropped(event -> onDragDroppedContainer(event, container));
            container.setClassroom((columns.get(i - 1).classroom()));
            container.setQuarter(columns.get(i - 1).quarter());
            container.setAxis(thematicAxis);
            
            pane.getChildren().add(container);
            GridPane.setConstraints(container, i, pane.getRowCount() - 1, 1, 1, HPos.CENTER, VPos.CENTER);
            componentsRow.boxes.add(container);
        }

        return componentsRow;
    }

    public static void removeGridRow(GridPane pane, ThematicAxis thematicAxis, ArrayList<ComponentsRow> list) {
        ComponentsRow row = list.stream().filter(t -> t.thematicAxis().equals(thematicAxis)).findFirst().get();

        pane.getChildren().removeAll(row.boxes());
        pane.getChildren().remove(row.relatedText());
        list.remove(row);
    }

    public static void onDragOverContainer(DragEvent event, FileViewerContainer target) {
        FileViewer source = ((FileViewer) event.getGestureSource());

        Dragboard db = event.getDragboard();
        boolean success = false;
        if (!db.hasContent(DataFormat.lookupMimeType("application/json"))) return;
        
        if (! target.getChildren().contains(source)) {
            success = true;
        }

        if (success) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
    }

    public static void onDragDroppedContainer(DragEvent event, FileViewerContainer target) {
        Dragboard db = event.getDragboard();
        if (! db.hasContent(DataFormat.lookupMimeType("application/json"))) return;

        FileViewer source = ((FileViewer) event.getGestureSource());
        FileViewerContainer parent = (FileViewerContainer) source.getParent();

        boolean success = true;

        parent.getChildren().remove(source);
        target.getChildren().add(source);

        source.getCompetency().setParentClassroom(parent.getClassroom());
        source.getCompetency().setParentSchoolYearQuarter(parent.getQuarter());
        source.getCompetency().setParentThematicAxis(parent.getAxis());

        event.setDropCompleted(success);
    }
    
    /*public static ComponentsRow moveThematicAxisAround(GridPane pane, ThematicAxis thematicAxis) {

    }*/
}
