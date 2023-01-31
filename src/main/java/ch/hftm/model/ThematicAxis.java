package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public class ThematicAxis {
    private StringProperty _name;
    private Integer _order;
    private ObservableSet<CoreCompetency> _coreCompetencies;

    public ObservableSet<CoreCompetency> getCoreCompetencies() {
        return _coreCompetencies;
    }

    public ThematicAxis(String name, Integer order) {
        this._name = new SimpleStringProperty(name);
        this._order = order;

        _coreCompetencies = FXCollections.observableSet();
    }

    public StringProperty nameProperty() {
        return _name;
    }

    public String getName() {
        return _name.get();
    }

    public void setName(String name) {
        this._name = new SimpleStringProperty(name);
    }

    public String toString() {
        return _name.get();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
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
        return true;
    }

}
