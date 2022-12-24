package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CoreCompetency {
    private StringProperty _name;
    private StringProperty _description;

    private Classroom _parentClassroom;
    private Lesson _parentLesson;
    private ThematicAxis _parentThematicAxis;
    private SchoolYearQuarter _parentSchoolYearQuarter;

    private Integer _order;

    public StringProperty nameProperty() {
        return _name;
    }

    public String getName() {
        return _name.getValue();
    }

    public CoreCompetency setDescription(String description) {
        _description = new SimpleStringProperty(description);

        return this;
    }

    public CoreCompetency setName(String name) {
        _name = new SimpleStringProperty(name);

        return this;
    }

    public StringProperty descriptionProperty() {
        return _description;
    }

    public String getDescription() {
        return _description.get(); 
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
        result = prime * result + ((_description == null) ? 0 : _description.hashCode());
        result = prime * result + ((_parentClassroom == null) ? 0 : _parentClassroom.hashCode());
        result = prime * result + ((_parentLesson == null) ? 0 : _parentLesson.hashCode());
        result = prime * result + ((_parentThematicAxis == null) ? 0 : _parentThematicAxis.hashCode());
        result = prime * result + ((_parentSchoolYearQuarter == null) ? 0 : _parentSchoolYearQuarter.hashCode());
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
        CoreCompetency other = (CoreCompetency) obj;
        if (_name == null) {
            if (other._name != null)
                return false;
        } else if (!_name.equals(other._name))
            return false;
        if (_description == null) {
            if (other._description != null)
                return false;
        } else if (!_description.equals(other._description))
            return false;
        if (_parentClassroom == null) {
            if (other._parentClassroom != null)
                return false;
        } else if (!_parentClassroom.equals(other._parentClassroom))
            return false;
        if (_parentLesson == null) {
            if (other._parentLesson != null)
                return false;
        } else if (!_parentLesson.equals(other._parentLesson))
            return false;
        if (_parentThematicAxis == null) {
            if (other._parentThematicAxis != null)
                return false;
        } else if (!_parentThematicAxis.equals(other._parentThematicAxis))
            return false;
        if (_parentSchoolYearQuarter == null) {
            if (other._parentSchoolYearQuarter != null)
                return false;
        } else if (!_parentSchoolYearQuarter.equals(other._parentSchoolYearQuarter))
            return false;
        return true;
    }
}
