package ch.hftm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

@XmlRootElement(name = "quarter")
public class SchoolYearQuarter {
    private IntegerProperty quarter = new SimpleIntegerProperty(0);
    private IntegerProperty startWeek = new SimpleIntegerProperty(0);
    private IntegerProperty endWeek = new SimpleIntegerProperty(0);
    
    public SchoolYearQuarter(Integer quarter, Integer startWeek, Integer endWeek) {
        this.quarter.set(quarter);
        this.startWeek.set(startWeek);
        this.endWeek.set(endWeek);
    }

    public SchoolYearQuarter() {
    }
    
    public Integer getStartWeek() {
        return startWeek.get();
    }
    
    @XmlElement(name = "startweek")
    public void setStartWeek(Integer startWeek) {
        this.startWeek.set(startWeek);
    }
    
    public IntegerProperty endWeekProperty() {
        return endWeek;
    } 

    public IntegerProperty startWeekProperty() {
        return startWeek;
    }

    public Integer getEndWeek() {
        return endWeek.get();
    }

    @XmlElement(name = "endweek")
    public void setEndWeek(Integer endWeek) {
        this.endWeek.set(endWeek);
    }

    public Integer getQuarter() {
        return quarter.get();
    }

    @XmlAttribute(name = "quarterid")
    public void setQuarter(Integer quarter) {
        this.quarter.set(quarter);
    }

    public IntegerProperty quarterProperty() {
        return quarter;
    }

    public String toString() {
        return String.format("Semaine %d à %d", getStartWeek(), getEndWeek()); 
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((quarter == null) ? 0 : quarter.hashCode());
        result = prime * result + ((startWeek == null) ? 0 : startWeek.hashCode());
        result = prime * result + ((endWeek == null) ? 0 : endWeek.hashCode());
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
        if (quarter == null) {
            if (other.quarter != null)
                return false;
        } else if (quarter.get() != other.quarter.get())
            return false;
        if (startWeek == null) {
            if (other.startWeek != null)
                return false;
        } else if (startWeek.get() != other.startWeek.get())
            return false;
        if (endWeek == null) {
            if (other.endWeek != null)
                return false;
        } else if (endWeek.get() != other.endWeek.get())
            return false;
        return true;
    }
}
