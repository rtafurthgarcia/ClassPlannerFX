package ch.hftm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import org.controlsfx.control.CheckTreeView;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import ch.hftm.ClassPlannerFX;
import ch.hftm.model.Context;
import ch.hftm.model.CoreCompetency;
import ch.hftm.model.SchoolYearQuarter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MainViewController {

    @FXML
    private TableView<CoreCompetency> tvMain;

    @FXML
    private VBox vbSpreadsheet;

    @FXML
    private TableColumn<CoreCompetency, String> tcQ1;
    @FXML
    private TableColumn<CoreCompetency, String> tcQ2;
    @FXML
    private TableColumn<CoreCompetency, String> tcQ3;
    @FXML
    private TableColumn<CoreCompetency, String> tcQ4;

    //ArrayList<TableColumn<CoreCompetency>> listOfSubColumns = new ArrayList()<TableColumn<CoreCompetency, String>>;
    ArrayList<TableColumn<CoreCompetency, String>> listOfSubColumns = new ArrayList<>();

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
        //_sharedContext.primaryStage.getScene().getStylesheets().add(ClassPlannerFX.class.getResource("/css/style.css").toExternalForm());
        setQuartalsColumns();
        setClassrooms();
    }

    public void setQuartalsColumns() {
        SchoolYearQuarter quarter1 = _sharedContext.schoolYearQuarters.stream().filter(t -> t.getQuarter() == 1).reduce((a, b) -> {
            throw new IllegalStateException("Multiple elements: " + a + ", " + b);
        }).get();
        tcQ1.setText(quarter1.toString());
        tcQ1.setUserData(quarter1);

        SchoolYearQuarter quarter2 = _sharedContext.schoolYearQuarters.stream().filter(t -> t.getQuarter() == 2).reduce((a, b) -> {
            throw new IllegalStateException("Multiple elements: " + a + ", " + b);
        }).get();

        tcQ2.setText(quarter2.toString());
        tcQ2.setUserData(quarter2);

        SchoolYearQuarter quarter3 = _sharedContext.schoolYearQuarters.stream().filter(t -> t.getQuarter() == 3).reduce((a, b) -> {
            throw new IllegalStateException("Multiple elements: " + a + ", " + b);
        }).get();

        tcQ3.setText(quarter3.toString());
        tcQ3.setUserData(quarter3);

        SchoolYearQuarter quarter4 = _sharedContext.schoolYearQuarters.stream().filter(t -> t.getQuarter() == 4).reduce((a, b) -> {
            throw new IllegalStateException("Multiple elements: " + a + ", " + b);
        }).get();

        tcQ4.setText(quarter4.toString());
        tcQ4.setUserData(quarter4);
    }

    public void setClassrooms() {
        Stream<TableColumn> quarterlyColumns = Stream.of(tcQ1, tcQ2, tcQ3, tcQ4);
        quarterlyColumns.forEach(tc -> {
            _sharedContext.classrooms.stream().forEach(c -> {
                TableColumn<CoreCompetency, String> tcNew = new TableColumn<>(c.getName());
                tc.getColumns().add(tcNew);
                tcNew.setUserData(quarterlyColumns);
                listOfSubColumns.add(tcNew);
            });
        });
    }

    public void setThematicAxis() {
        
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