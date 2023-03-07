package ch.hftm.model;

import java.io.File;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@XmlRootElement(name = "corecompetency")
public class CoreCompetency extends SchoolUnit<SchoolUnit<?>> implements Cloneable {

    public CoreCompetency(String name) {
        super(name, FXCollections.observableArrayList());

        isPartOfTreeView = true;
    }

    public CoreCompetency() {
        super("null", FXCollections.observableArrayList());

        isPartOfTreeView = true;
    }

    private StringProperty description = new SimpleStringProperty("");

    private Classroom parentClassroom;
    private ThematicAxis parentThematicAxis;
    private SchoolYearQuarter parentSchoolYearQuarter;

    private ListProperty<File> files = new SimpleListProperty<File>(FXCollections.observableArrayList());

    private boolean isPartOfTreeView;

    @XmlElementWrapper(name="files")
    @XmlElements({
            @XmlElement(name="file", type=File.class),
    })
    public ObservableList<File> getFiles() {
        return files;
    }

    public CoreCompetency setFiles(ObservableList<File> files) {
        this.files.set(files);

        return this;
    }

    public ListProperty<File> filesProperty() {
        return files;
    }

    public boolean isPartOfTreeView() {
        return isPartOfTreeView;
    }

    @XmlAttribute(name = "treeview")
    public CoreCompetency setPartOfTreeView(boolean isPartOfTreeView) {
        this.isPartOfTreeView = isPartOfTreeView;

        return this;
    }

    @XmlElement(name = "description")
    public CoreCompetency setDescription(String description) {
        this.description = new SimpleStringProperty(description);

        return this;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get(); 
    }

    public Classroom getParentClassroom() {
        return parentClassroom;
    }

    @XmlElement(type = Classroom.class, name = "classroom")
    public CoreCompetency setParentClassroom(Classroom parentClassroom) {
        this.parentClassroom = parentClassroom;

        return this;
    }

    public ThematicAxis getParentThematicAxis() {
        return parentThematicAxis;
    }

    @XmlElement(type = ThematicAxis.class, name = "thematicaxis")
    public CoreCompetency setParentThematicAxis(ThematicAxis parentThematicAxis) {
        this.parentThematicAxis = parentThematicAxis.clone();

        return this;
    }

    public SchoolYearQuarter getParentSchoolYearQuarter() {
        return parentSchoolYearQuarter;
    }

    @XmlElement(type = SchoolYearQuarter.class, name = "quarter")
    public CoreCompetency setParentSchoolYearQuarter(SchoolYearQuarter parentSchoolYearQuarter) {
        this.parentSchoolYearQuarter = parentSchoolYearQuarter;

        return this;
    }

    @Override
    public CoreCompetency clone() {
        CoreCompetency competency = new CoreCompetency(this.getName());
        competency.nameProperty().bindBidirectional(this.nameProperty());
        competency.descriptionProperty().bindBidirectional(this.descriptionProperty());
        competency.filesProperty().bindBidirectional(this.filesProperty());
        //competency.files.addAll(this.files);

        return competency;
    }

    public CoreCompetency cloneForGrid() {
        CoreCompetency competency = new CoreCompetency(this.getName());
        competency.nameProperty().bindBidirectional(this.nameProperty());
        competency.descriptionProperty().bindBidirectional(this.descriptionProperty());
        competency.parentClassroom = this.parentClassroom;
        competency.parentThematicAxis = this.parentThematicAxis;
        competency.parentSchoolYearQuarter = this.parentSchoolYearQuarter;
        competency.filesProperty().bindBidirectional(this.filesProperty());
        competency.isPartOfTreeView = false;

        return competency;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((description.get() == null) ? 0 : description.get().hashCode());
        result = prime * result + ((super.name.get() == null) ? 0 : super.name.get().hashCode());
        result = prime * result + (isPartOfTreeView ? 1231 : 1237);
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
