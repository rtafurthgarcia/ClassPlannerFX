package ch.hftm.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SchoolYear extends SchoolUnit<Lesson> {
    private ObjectProperty<LocalDate> startDay;
    private ObjectProperty<LocalDate> endDay;
    private BooleanProperty archived;
    private ObservableList<SchoolYearQuarter> quarters;

    private final String YEAR_PATTERN = "yyyy"; 
    transient private DateTimeFormatter yearlyFormat = new DateTimeFormatterBuilder()
        .appendPattern(YEAR_PATTERN)
        .toFormatter();

    public ObservableList<SchoolYearQuarter> getQuarters() {
        return quarters;
    }

    public SchoolYear(LocalDate startDay, LocalDate endDay) {
        super(startDay.getYear() + "-" + startDay.getYear(), FXCollections.observableArrayList(), Lesson::new);
        
        this.startDay = new SimpleObjectProperty<LocalDate>(startDay);
        this.endDay =  new SimpleObjectProperty<LocalDate>(endDay);
        
        this.archived = new SimpleBooleanProperty(false);

        this.quarters = FXCollections.observableArrayList();
    }

    public SchoolYear(String rangeToParse) {
        super(rangeToParse, FXCollections.observableArrayList(), Lesson::new);    

        this.startDay = new SimpleObjectProperty<LocalDate>(LocalDate.parse(rangeToParse.subSequence(0, 3).toString(), yearlyFormat));
        this.endDay = new SimpleObjectProperty<LocalDate>(LocalDate.parse(rangeToParse.subSequence(5, 8).toString(), yearlyFormat));

        this.archived = new SimpleBooleanProperty(false);

        this.quarters = FXCollections.observableArrayList();
    }

    public ObjectProperty<LocalDate> startDayProperty() {
        return startDay;
    }

    public SchoolYear setStartDay(LocalDate startDay) {
        this.startDay = new SimpleObjectProperty<LocalDate>(startDay);

        return this;
    }

    public ObjectProperty<LocalDate> endDayProperty() {
        return endDay;
    }

    public SchoolYear setEndDay(LocalDate endDay) {
        this.endDay = new SimpleObjectProperty<LocalDate>(endDay);

        return this;
    }

    public BooleanProperty archivedProperty() {
        return archived;
    }

    public boolean getArchived() {
        return archived.get();
    }

    public SchoolYear setArchived(boolean archived) {
        this.archived = new SimpleBooleanProperty(archived);

        return this;
    }

    public LocalDate getEndDay() {
        return endDay.get();
    }

    public LocalDate getStartDay() {
        return startDay.get();
    }

    @Override
    public String toString() {
        return startDay.get().getYear() + "-" + endDay.get().getYear();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((startDay == null) ? 0 : startDay.hashCode());
        result = prime * result + ((endDay == null) ? 0 : endDay.hashCode());
        result = prime * result + ((archived == null) ? 0 : archived.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SchoolYear other = (SchoolYear) obj;
        if (startDay == null) {
            if (other.startDay != null)
                return false;
        } else if (!startDay.equals(other.startDay))
            return false;
        if (endDay == null) {
            if (other.endDay != null)
                return false;
        } else if (!endDay.equals(other.endDay))
            return false;
        if (archived == null) {
            if (other.archived != null)
                return false;
        } else if (!archived.equals(other.archived))
            return false;
        return true;
    }

}
