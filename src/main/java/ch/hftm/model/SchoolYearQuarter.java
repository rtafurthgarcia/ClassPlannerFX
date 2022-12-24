package ch.hftm.model;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SchoolYearQuarter {
    private ObjectProperty<LocalDate> _startWeek;
    private ObjectProperty<LocalDate> _endWeek;
    
    public SchoolYearQuarter(LocalDate startWeek, LocalDate endWeek) {
        this._startWeek = new SimpleObjectProperty<>(startWeek);
        this._endWeek = new SimpleObjectProperty<>(endWeek);
    }
    
    public LocalDate getStartWeek() {
        return _startWeek.get();
    }
    
    public void setStartWeek(LocalDate startWeek) {
        this._startWeek = new SimpleObjectProperty<>(startWeek);
    }
    
    public ObjectProperty<LocalDate> endWeekProperty() {
        return _endWeek;
    } 

    public ObjectProperty<LocalDate> startWeekProperty() {
        return _startWeek;
    }

    public LocalDate getEndWeek() {
        return _endWeek.get();
    }

    public void setEndWeek(LocalDate endWeek) {
        this._endWeek = new SimpleObjectProperty<>(endWeek);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_startWeek == null) ? 0 : _startWeek.hashCode());
        result = prime * result + ((_endWeek == null) ? 0 : _endWeek.hashCode());
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
        SchoolYearQuarter other = (SchoolYearQuarter) obj;
        if (_startWeek == null) {
            if (other._startWeek != null)
                return false;
        } else if (!_startWeek.equals(other._startWeek))
            return false;
        if (_endWeek == null) {
            if (other._endWeek != null)
                return false;
        } else if (!_endWeek.equals(other._endWeek))
            return false;
        return true;
    }
}
