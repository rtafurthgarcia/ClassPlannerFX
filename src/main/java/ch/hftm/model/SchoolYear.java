package ch.hftm.model;

import java.util.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SchoolYear {
    private ObjectProperty<Date> _startDay;
    private ObjectProperty<Date> _endDay;
    private BooleanProperty _archived;

    public SchoolYear(Date date, Date date2) {
        this._startDay = new SimpleObjectProperty<Date>(date);
        this._endDay =  new SimpleObjectProperty<Date>(date2);

        this._archived = new SimpleBooleanProperty(false);
    }

    public ObjectProperty<Date> startDayProperty() {
        return _startDay;
    }

    public SchoolYear setStartDay(Date startDay) {
        this._startDay = new SimpleObjectProperty<Date>(startDay);

        return this;
    }

    public ObjectProperty<Date> endDayProperty() {
        return _endDay;
    }

    public SchoolYear setEndDay(Date endDay) {
        this._endDay = new SimpleObjectProperty<Date>(endDay);

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

    public Date getEndDay() {
        return _endDay.get();
    }

    public Date getStartDay() {
        return _startDay.get();
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
