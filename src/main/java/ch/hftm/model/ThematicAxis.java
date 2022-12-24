package ch.hftm.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ThematicAxis {
    private StringProperty _name;
    private Integer _order;

    private Lesson _parentLesson;
    private SchoolYear _parentSchoolYear;

    public ThematicAxis(StringProperty _name, Integer _order, Lesson _parentLesson, SchoolYear _parentSchoolYear) {
        this._name = _name;
        this._order = _order;
        this._parentLesson = _parentLesson;
        this._parentSchoolYear = _parentSchoolYear;
    }

    public StringProperty nameProperty() {
        return _name;
    }

    public String getName() {
        return _name.get();
    }

    public void setName(StringProperty name) {
        this._name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
        result = prime * result + ((_parentLesson == null) ? 0 : _parentLesson.hashCode());
        result = prime * result + ((_parentSchoolYear == null) ? 0 : _parentSchoolYear.hashCode());
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
        ThematicAxis other = (ThematicAxis) obj;
        if (_name == null) {
            if (other._name != null)
                return false;
        } else if (!_name.equals(other._name))
            return false;
        if (_parentLesson == null) {
            if (other._parentLesson != null)
                return false;
        } else if (!_parentLesson.equals(other._parentLesson))
            return false;
        if (_parentSchoolYear == null) {
            if (other._parentSchoolYear != null)
                return false;
        } else if (!_parentSchoolYear.equals(other._parentSchoolYear))
            return false;
        return true;
    }

}
