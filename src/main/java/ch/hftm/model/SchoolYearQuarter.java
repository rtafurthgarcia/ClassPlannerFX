package ch.hftm.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.WeekFields;

import ch.hftm.util.TypeHelper;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

@XmlRootElement(name = "quarter")
public class SchoolYearQuarter {
    private IntegerProperty quarter = new SimpleIntegerProperty(0);
    private ObjectProperty<LocalDate> startWeek = new SimpleObjectProperty<>(LocalDate.now());
    private ObjectProperty<LocalDate> endWeek = new SimpleObjectProperty<>(LocalDate.now());

    @XmlTransient
    private final String YEAR_PATTERN = "yyyy"; 
    @XmlTransient
    private DateTimeFormatter yearlyFormat = new DateTimeFormatterBuilder()
        .appendPattern(YEAR_PATTERN)
        .toFormatter();
    
    public SchoolYearQuarter(Integer quarter, LocalDate startWeek, LocalDate endWeek) {
        this.quarter.set(quarter);
        this.startWeek.set(startWeek);
        this.endWeek.set(endWeek);
    }

    public SchoolYearQuarter() {
    }
    
    public LocalDate getStartWeek() {
        return startWeek.get();
    }
    
    @XmlElement(name = "startweek")
    @XmlJavaTypeAdapter(value = TypeHelper.LocalDateAdapter.class)
    public void setStartWeek(LocalDate startWeek) {
        this.startWeek.set(startWeek);
    }
    
    public ObjectProperty<LocalDate> endWeekProperty() {
        return endWeek;
    } 

    public ObjectProperty<LocalDate> startWeekProperty() {
        return startWeek;
    }

    public LocalDate getEndWeek() {
        return endWeek.get();
    }

    @XmlElement(name = "endweek")
    @XmlJavaTypeAdapter(value = TypeHelper.LocalDateAdapter.class)
    public void setEndWeek(LocalDate endWeek) {
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
        return String.format(
            "Semaine %d à %d - %d", 
            getStartWeek().get(WeekFields.of(yearlyFormat.getLocale()).weekOfYear()), 
            getEndWeek().get(WeekFields.of(yearlyFormat.getLocale()).weekOfYear()),
            getEndWeek().getYear()); 
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
