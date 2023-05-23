package ch.hftm.util;

import java.time.LocalDate;

import ch.hftm.model.SchoolYear;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;

public class SchoolYearConverter extends StringConverter<SchoolYear> {
    @Override
    public String toString(SchoolYear schema) {
        return schema.toString();
    }

    @Override
    public SchoolYear fromString(String string) {
        try {
            return new SchoolYear(string);
        } catch (Exception e) {
            Alert alert = new Alert(
                    AlertType.ERROR,
                    "Invalid year format. Please make sure it matches the following format: '####-####'.");
            alert.setHeaderText("Invalid year format");
            alert.setTitle("Year format");
            alert.showAndWait();

            String newYear = String.format("%d-%d", LocalDate.now().getYear(), LocalDate.now().plusYears(1).getYear()); 
            return new SchoolYear(newYear);
        }
    }
}
