module ch.hftm {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.controlsfx.controls;
    requires org.hildan.fxgson;
    requires com.google.gson;
    requires org.girod.javafx.svgimage;

    exports ch.hftm;

    opens ch.hftm.controller to javafx.fxml;
    opens ch.hftm.component to javafx.fxml;

    opens ch.hftm.model to com.google.gson;
    //opens ch.hftm.model to javafx.fxml;
}