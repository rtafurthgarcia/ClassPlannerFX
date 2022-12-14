module ch.hftm {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.fxml;

    exports ch.hftm;

    opens ch.hftm.controller to javafx.fxml;
    //opens ch.hftm.model to javafx.fxml;
}