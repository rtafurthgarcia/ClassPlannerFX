package ch.hftm.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlRootElement(name = "classroom")
public class Classroom {
    private StringProperty name = new SimpleStringProperty("null");

    public Classroom(String name) {
        this.name.set(name);
    }

    public Classroom() {
    }

    public StringProperty nameProperty() {
        return name;
    }

    @XmlValue()
    public Classroom setName(String name) {
        this.name.set(name);

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
