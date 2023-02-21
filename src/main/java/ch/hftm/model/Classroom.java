package ch.hftm.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Classroom {
    private StringProperty name;

    public Classroom(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Classroom setName(StringProperty name) {
        this.name = name;

        return this;
    }

    public String getName() {
        return name.get();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Classroom other = (Classroom) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
