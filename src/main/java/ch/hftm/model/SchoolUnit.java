package ch.hftm.model;

import java.util.function.Function;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

abstract public class SchoolUnit<T extends SchoolUnit<?>> {
    
    protected final StringProperty name = new SimpleStringProperty("");
    
    protected final Function<String, T> subUnitSupplier;
    
    protected final ObservableList<T> subUnits;

    protected SchoolUnit(String name, ObservableList<T> subUnits, Function<String, T> subUnitSupplier) {
        this.subUnits = subUnits;
        this.subUnitSupplier = subUnitSupplier ;
        setName(name);
    }

    protected SchoolUnit(String name, ObservableList<T> subUnits) {
        this(name, subUnits, n -> null);
    }
    
    protected SchoolUnit(String name, Function<String, T> subUnitSupplier) {
        this(name, FXCollections.observableArrayList(), subUnitSupplier);
    }
    
    protected SchoolUnit(String name) {
        this(name, FXCollections.observableArrayList(), n -> null) ;
    }

    public final StringProperty nameProperty() {
        return this.name;
    }
    
    public final String getName() {
        return this.nameProperty().get();
    }
    
    public final void setName(final String name) {
        this.nameProperty().set(name);
    }

    public ObservableList<T> getSubUnits() {
        return subUnits;
    }
    
    public SchoolUnit<?> createAndAddSubUnit(String name) {
        getSubUnits().add(subUnitSupplier.apply(name));

        return this;
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
        SchoolUnit other = (SchoolUnit) obj;
        if (name == null) {
            if (other.getName() != null)
                return false;
        } else if (!getName().equals(other.getName()))
            return false;
        return true;
    }
}