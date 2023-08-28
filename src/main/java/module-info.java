module io.github.zechiax.builkrenamerapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens io.github.zechiax.builkrenamerapp to javafx.fxml;
    exports io.github.zechiax.builkrenamerapp;
}