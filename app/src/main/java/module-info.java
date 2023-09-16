module io.github.zechiax.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires java.logging;
    requires org.jetbrains.annotations;


    opens io.github.zechiax.app to javafx.fxml;
    exports io.github.zechiax.app;
    exports io.github.zechiax.app.core;
}