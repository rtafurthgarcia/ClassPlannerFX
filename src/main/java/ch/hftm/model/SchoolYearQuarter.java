package ch.hftm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

@XmlRootElement(name = "quarter")
public class SchoolYearQuarter {
    private IntegerProperty _quarter;
    private IntegerProperty _startWeek;
    private IntegerProperty _endWeek;
    
    public SchoolYearQuarter(Integer quarter, Integer startWeek, Integer endWeek) {
        this._quarter = new SimpleIntegerProperty(quarter);
        this._startWeek = new SimpleIntegerProperty(startWeek);
        this._endWeek = new SimpleIntegerProperty(endWeek);
    }

    public SchoolYearQuarter() {
        this._quarter = new SimpleIntegerProperty(0);
        this._startWeek = new SimpleIntegerProperty(0);
        this._endWeek = new SimpleIntegerProperty(0);
    }
    
    public Integer getStartWeek() {
        return _startWeek.get();
    }
    
    @XmlElement(name = "startweek")
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

    @XmlElement(name = "endweek")
    public void setEndWeek(Integer endWeek) {
        this._endWeek = new SimpleIntegerProperty(endWeek);
    }

    public Integer getQuarter() {
        return _quarter.get();
    }

    @XmlAttribute(name = "quarterid")
    public void setQuarter(Integer quarter) {
        this._quarter = new SimpleIntegerProperty(quarter);
    }

    public IntegerProperty quarterProperty() {
        return _quarter;
    }

    public String toString() {
        return String.format("Semaine %d à %d", getStartWeek(), getEndWeek()); 
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_quarter == null) ? 0 : _quarter.hashCode());
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
        if (_quarter == null) {
            if (other._quarter != null)
                return false;
        } else if (!_quarter.equals(other._quarter))
            return false;
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
