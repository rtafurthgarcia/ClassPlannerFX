package ch.hftm.util;

import ch.hftm.component.FileViewer;
import ch.hftm.model.ThematicAxis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GridPaneHelper {
    static final int COLUMN_STARTING_INDEX = 0;

    public record ComponentsRow(int gridPaneIndex, Text relatedText, ObservableList<VBox> boxes, ThematicAxis thematicAxis) { }

    public static ComponentsRow addGridRow(GridPane pane, ThematicAxis thematicAxis) {
        final int COLUMN_MAX = pane.getColumnCount() - 1;

        pane.getRowConstraints().add(new RowConstraints());
            
        Text tNewThematicAxis = new Text();
        tNewThematicAxis.textProperty().bind(thematicAxis.nameProperty());

        pane.getChildren().add(tNewThematicAxis);
        GridPane.setConstraints(tNewThematicAxis, COLUMN_STARTING_INDEX, pane.getRowCount(), 1, 1, HPos.CENTER, VPos.CENTER);
        
        ComponentsRow componentsRow = new ComponentsRow(pane.getRowCount(), tNewThematicAxis, FXCollections.observableArrayList(), thematicAxis);
        
        for (int i = COLUMN_STARTING_INDEX + 1; i < COLUMN_MAX; i++) {
            VBox vbox = new VBox();
            vbox.setMinSize(120, 180);
            vbox.setOnDragOver(event -> {
                FileViewer source = ((FileViewer) event.getGestureSource());

                Dragboard db = event.getDragboard();
                boolean success = false;
                if (!db.hasContent(DataFormat.lookupMimeType("application/json"))) return;
                
                if (! vbox.getChildren().contains(source)) {
                    success = true;
                }

                if (success) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            });

            vbox.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (! db.hasContent(DataFormat.lookupMimeType("application/json"))) return;
        
                FileViewer source = ((FileViewer) event.getGestureSource());
                VBox parent = (VBox) source.getParent();
        
                boolean success = true;
        
                parent.getChildren().remove(source);
                vbox.getChildren().add(source);
        
                event.setDropCompleted(success);
            });
            
            pane.getChildren().add(vbox);
            GridPane.setConstraints(vbox, i, pane.getRowCount() - 1, 1, 1, HPos.CENTER, VPos.CENTER);
            componentsRow.boxes.add(vbox);
        }

        return componentsRow;
    }

    //public static ComponentsRow moveThematicAxisAround(GridPane pane, ThematicAxis thematicAxis) {
    public static void removeGridRow(GridPane pane, ThematicAxis thematicAxis, ObservableList<ComponentsRow> list) {
        ComponentsRow row = list.stream().filter(t -> t.thematicAxis().equals(thematicAxis)).findFirst().get();

        pane.getChildren().removeAll(row.boxes());
        pane.getChildren().remove(row.relatedText());
        list.remove(row);
    }

}
