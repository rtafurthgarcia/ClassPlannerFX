package ch.hftm.model;

import java.io.File;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@XmlRootElement(name = "corecompetency")
public class CoreCompetency extends SchoolUnit<SchoolUnit<?>> implements Cloneable {
    private StringProperty description = new SimpleStringProperty("");
    private ObjectProperty<Intersection> intersection = new SimpleObjectProperty<>(new Intersection());
    private ObservableList<File> files = FXCollections.observableArrayList();
    private BooleanProperty isPartOfTreeView = new SimpleBooleanProperty(true);

    public CoreCompetency(String name) {
        super(name, FXCollections.observableArrayList());
    }

    public CoreCompetency() {
        super("null", FXCollections.observableArrayList());
    }

    public Intersection getIntersection() {
        return intersection.get();
    }

    public CoreCompetency setIntersection(Intersection intersection) {
        this.intersection.set(intersection);

        return this;
    }

    public ObjectProperty<Intersection> intersectionProperty() {
        return intersection;
    }

    @XmlElementWrapper(name="files")
    @XmlElements({
            @XmlElement(name="file", type=File.class),
    })
    public ObservableList<File> getFiles() {
        return files;
    }

    public CoreCompetency setFiles(ObservableList<File> files) {
        this.files.setAll(files);

        return this;
    }

    public ObservableList<File> filesProperty() {
        return files;
    }

    public boolean isPartOfTreeView() {
        return isPartOfTreeView.get();
    }

    @XmlAttribute(name = "treeview")
    public CoreCompetency setPartOfTreeView(boolean isPartOfTreeView) {
        this.isPartOfTreeView.set(isPartOfTreeView);;

        return this;
    }

    public BooleanProperty isPartOfTreeViewProperty() {
        return isPartOfTreeView;
    }

    @XmlElement(name = "description")
    public CoreCompetency setDescription(String description) {
        this.description.set(description);

        return this;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get(); 
    }


    @Override
    public CoreCompetency clone() {
        CoreCompetency competency = new CoreCompetency(this.getName());
        competency.setIntersection(this.getIntersection());
        competency.nameProperty().bindBidirectional(this.nameProperty());
        competency.descriptionProperty().bindBidirectional(this.descriptionProperty());

        competency.setFiles(this.getFiles());
        Bindings.bindContentBidirectional(this.files, competency.files);

        return competency;
    }

    public CoreCompetency cloneForDifferentThematicAxis() {
        CoreCompetency competency = new CoreCompetency(this.getName());
        competency.setIntersection(this.getIntersection());
        //competency.nameProperty().bindBidirectional(this.nameProperty());
        competency.setName(this.getName());
        competency.setDescription(this.getDescription());
        //competency.descriptionProperty().bindBidirectional(this.descriptionProperty());

        return competency;
    }

    public CoreCompetency cloneForGrid() {
        CoreCompetency competency = new CoreCompetency(this.getName());
        competency.nameProperty().bindBidirectional(this.nameProperty());
        competency.setIntersection(this.getIntersection());
        competency.descriptionProperty().bindBidirectional(this.descriptionProperty());
        competency.setFiles(this.getFiles());
        Bindings.bindContentBidirectional(this.files, competency.files);
        competency.isPartOfTreeView.set(false);

        return competency;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((description.get() == null) ? 0 : description.get().hashCode());
        result = prime * result + ((super.name.get() == null) ? 0 : super.name.get().hashCode());
        result = prime * result + (isPartOfTreeView() ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        CoreCompetency other = (CoreCompetency) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.get().equals(other.description.get()))
            return false;
        if (name.get() == null) {
            if (other.name.get() != null)
                return false;
        } else if (!name.get().equals(other.name.get()))
            return false;
        return true;
    }
}
