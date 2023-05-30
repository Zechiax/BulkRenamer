module com.example.bulkrenamer {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens com.example.bulkrenamer to javafx.fxml;
    exports com.example.bulkrenamer;
}