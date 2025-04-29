module org.coursework.messengerclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens org.coursework.messengerclient to javafx.fxml;
    exports org.coursework.messengerclient;
}