package ch.hftm.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@XmlRootElement(name = "school")
public class School extends SchoolUnit<SchoolYear> {

    @XmlElement(name = "author")
    private StringProperty author = new SimpleStringProperty("");
    private StringProperty comment = new SimpleStringProperty("");

    public String getAuthor() {
        return author.get();
    }

    public School setAuthor(String author) {
        this.author.set(author);

        return this;
    }

    public StringProperty authorProperty() {
        return author;
    }
    
    public School(String name) {
        super(name, FXCollections.observableArrayList(), SchoolYear::new);
    }

    public School() {
        super("null", FXCollections.observableArrayList(), SchoolYear::new);
    }

    public String getComment() {
        return comment.get();
    }

    @XmlElement(name = "comment")
    public School setComment(String comment) {
        this.comment.set(comment);

        return this;
    }

    public StringProperty commentProperty() {
        return comment;
    }
}
