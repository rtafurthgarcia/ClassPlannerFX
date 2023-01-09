module ch.hftm {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.controlsfx.controls;

    exports ch.hftm;

    opens ch.hftm.controller to javafx.fxml;
    //opens ch.hftm.model to javafx.fxml;
}