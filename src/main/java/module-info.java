module ch.hftm {
    requires java.logging;
    requires javafx.controls;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.controlsfx.controls;
    requires jakarta.activation;
    requires jakarta.xml.bind;
    requires org.girod.javafx.svgimage;

    exports ch.hftm;

    opens ch.hftm.controller to javafx.fxml;
    opens ch.hftm.component to javafx.fxml;

    opens ch.hftm.model to jakarta.xml.bind;
    opens ch.hftm.util to jakarta.xml.bind, com.sun.xml.bind.core;
    //opens java.base to com.google.gson;
}