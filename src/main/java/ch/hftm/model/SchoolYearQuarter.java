package ch.hftm.model;

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SchoolYearQuarter {
    private IntegerProperty _startWeek;
    private IntegerProperty _endWeek;
    
    public SchoolYearQuarter(Integer startWeek, Integer endWeek) {
        this._startWeek = new SimpleIntegerProperty(startWeek);
        this._endWeek = new SimpleIntegerProperty(endWeek);
    }
    
    public Integer getStartWeek() {
        return _startWeek.get();
    }
    
    public void setStartWeek(Integer startWeek) {
        this._startWeek = new SimpleIntegerProperty(startWeek);
    }
    
    public IntegerProperty endWeekProperty() {
        return _endWeek;
    } 

    public IntegerProperty startWeekProperty() {
        return _startWeek;
    }

    public Integer getEndWeek() {
        return _endWeek.get();
    }

    public void setEndWeek(Integer endWeek) {
        this._endWeek = new SimpleIntegerProperty(endWeek);
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
