package ch.hftm.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import ch.hftm.util.TypeHelper;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@XmlRootElement(name = "schoolyear")
public class SchoolYear extends SchoolUnit<Lesson> {
    private ObjectProperty<LocalDate> startDay = new SimpleObjectProperty<>(LocalDate.now());
    private ObjectProperty<LocalDate> endDay = new SimpleObjectProperty<>(LocalDate.now());
    private BooleanProperty archived = new SimpleBooleanProperty(false);

    private ObservableList<SchoolYearQuarter> quarters = FXCollections.observableArrayList();

    @XmlTransient
    private final String YEAR_PATTERN = "yyyy"; 
    @XmlTransient
    private DateTimeFormatter yearlyFormat = new DateTimeFormatterBuilder()
        .appendPattern(YEAR_PATTERN)
        .toFormatter();

    @XmlElementWrapper(name = "quarters")
    @XmlElements({
        @XmlElement(name="quarter",type=SchoolYearQuarter.class),
    })
    public ObservableList<SchoolYearQuarter> getQuarters() {
        return quarters;
    }

    public SchoolYear(LocalDate startDay, LocalDate endDay) {
        super(startDay.getYear() + "-" + startDay.getYear(), FXCollections.observableArrayList(), Lesson::new);
        
        this.startDay.set(startDay);
        this.endDay.set(endDay);
    }

    public SchoolYear(String rangeToParse) {
        super(rangeToParse, FXCollections.observableArrayList(), Lesson::new);    

        this.startDay.set(LocalDate.parse(rangeToParse.subSequence(0, 3).toString(), yearlyFormat));
        this.endDay.set(LocalDate.parse(rangeToParse.subSequence(5, 8).toString(), yearlyFormat));
    }

    public SchoolYear() {
        super("null", FXCollections.observableArrayList(), Lesson::new);    
    }

    public ObjectProperty<LocalDate> startDayProperty() {
        return startDay;
    }

    @XmlElement(name = "startday")
    @XmlJavaTypeAdapter(value = TypeHelper.LocalDateAdapter.class)
    public SchoolYear setStartDay(LocalDate startDay) {
        this.startDay.set(startDay);
        setName(startDay.getYear() + "-" + endDay.get().getYear());

        return this;
    }

    public ObjectProperty<LocalDate> endDayProperty() {
        return endDay;
    }

    @XmlElement(name = "endday")
    @XmlJavaTypeAdapter(value = TypeHelper.LocalDateAdapter.class)
    public SchoolYear setEndDay(LocalDate endDay) {
        this.endDay.set(endDay);
        setName(startDay.get().getYear() + "-" + endDay.getYear());

        return this;
    }

    public BooleanProperty archivedProperty() {
        return archived;
    }

    public boolean getArchived() {
        return archived.get();
    }

    @XmlAttribute(name = "archived")
    public SchoolYear setArchived(boolean archived) {
        this.archived.set(archived);

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
        } else if (!startDay.get().equals(other.startDay.get()))
            return false;
        if (endDay == null) {
            if (other.endDay != null)
                return false;
        } else if (!endDay.get().equals(other.endDay.get()))
            return false;
        if (archived == null) {
            if (other.archived != null)
                return false;
        } else if (archived.get() != other.archived.get())
            return false;
        return true;
    }

}
