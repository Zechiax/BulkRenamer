module io.github.zechiax.builkrenamerapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires java.logging;
    requires org.jetbrains.annotations;


    opens io.github.zechiax.builkrenamerapp to javafx.fxml;
    exports io.github.zechiax.builkrenamerapp;
    exports io.github.zechiax.builkrenamerapp.core;
}