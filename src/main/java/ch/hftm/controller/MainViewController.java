package ch.hftm.controller;

import org.controlsfx.control.CheckTreeView;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import ch.hftm.ClassPlannerFX;
import ch.hftm.model.Context;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MainViewController {

    @FXML
    private TableView tvMain;

    @FXML
    private VBox vbSpreadsheet;

    @FXML
    private CheckTreeView ctvMain;

    private Context _sharedContext = Context.getInstance();

    /*private void createMainSpreadsheet() {
        int rowCount = 15;
        int columnCount = 8;
        GridBase grid = new GridBase(rowCount, columnCount);
        grid.getColumnHeaders().setAll("1. Klasse", "2. Klasse", "1. Klasse", "2. Klasse", "1. Klasse", "2. Klasse", "1. Klasse", "2. Klasse");
        
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for (int row = 0; row < grid.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            for (int column = 0; column < grid.getColumnCount(); ++column) {
                list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"value"));
            }
            rows.add(list);
        }
        grid.setRows(rows);
   
        svMain = new SpreadsheetView(grid);
        vbSpreadsheet.getChildren().addAll(svMain);

        vbSpreadsheet.prefWidthProperty().bind(svMain.widthProperty());
        //vbSpreadsheet.prefHeightProperty().bind(svMain.heightProperty());
    }*/

    @FXML
    public void initialize() {
        
    }

    @FXML
    void onClose() {
        Context.getInstance().primaryStage.close();
    }

    @FXML
    public void onOpenSettings() {
        _sharedContext.showSettingsView();
    }
}