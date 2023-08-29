module io.github.zechiax.builkrenamerapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires org.apache.commons.lang3;


    opens io.github.zechiax.builkrenamerapp to javafx.fxml;
    exports io.github.zechiax.builkrenamerapp;
    exports io.github.zechiax.builkrenamerapp.core;
}