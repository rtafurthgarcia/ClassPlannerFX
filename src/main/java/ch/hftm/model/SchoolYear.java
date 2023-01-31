package ch.hftm.model;

import java.time.LocalDate;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public class SchoolYear {
    private ObjectProperty<LocalDate> _startDay;
    private ObjectProperty<LocalDate> _endDay;
    private BooleanProperty _archived;
    private ObservableSet<Lesson> _yearlyLessons;
    private ObservableSet<SchoolYearQuarter> _quarters;

    public ObservableSet<Lesson> getYearsLessons() {
        return _yearlyLessons;
    }

    public ObservableSet<SchoolYearQuarter> getQuarters() {
        return _quarters;
    }

    public SchoolYear(LocalDate startDay, LocalDate endDay) {
        this._startDay = new SimpleObjectProperty<LocalDate>(startDay);
        this._endDay =  new SimpleObjectProperty<LocalDate>(endDay);

        this._archived = new SimpleBooleanProperty(false);

        this._yearlyLessons = FXCollections.observableSet();
        this._quarters = FXCollections.observableSet();
    }

    public ObjectProperty<LocalDate> startDayProperty() {
        return _startDay;
    }

    public SchoolYear setStartDay(LocalDate startDay) {
        this._startDay = new SimpleObjectProperty<LocalDate>(startDay);

        return this;
    }

    public ObjectProperty<LocalDate> endDayProperty() {
        return _endDay;
    }

    public SchoolYear setEndDay(LocalDate endDay) {
        this._endDay = new SimpleObjectProperty<LocalDate>(endDay);

        return this;
    }

    public BooleanProperty archivedProperty() {
        return _archived;
    }

    public boolean getArchived() {
        return _archived.get();
    }

    public SchoolYear setArchived(boolean archived) {
        this._archived = new SimpleBooleanProperty(archived);

        return this;
    }

    public LocalDate getEndDay() {
        return _endDay.get();
    }

    public LocalDate getStartDay() {
        return _startDay.get();
    }

    public String toString() {
        return _startDay.get().getYear() + "-" + _endDay.get().getYear();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_startDay == null) ? 0 : _startDay.hashCode());
        result = prime * result + ((_endDay == null) ? 0 : _endDay.hashCode());
        result = prime * result + ((_archived == null) ? 0 : _archived.hashCode());
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
        if (_startDay == null) {
            if (other._startDay != null)
                return false;
        } else if (!_startDay.equals(other._startDay))
            return false;
        if (_endDay == null) {
            if (other._endDay != null)
                return false;
        } else if (!_endDay.equals(other._endDay))
            return false;
        if (_archived == null) {
            if (other._archived != null)
                return false;
        } else if (!_archived.equals(other._archived))
            return false;
        return true;
    }

}
