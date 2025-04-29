module org.coursework.messenger {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;

    opens org.coursework.messenger to javafx.fxml;
    exports org.coursework.messenger;
}