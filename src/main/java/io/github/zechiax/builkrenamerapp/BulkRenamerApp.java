package io.github.zechiax.builkrenamerapp;

import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.logging.*;


import java.io.IOException;

public class BulkRenamerApp extends Application {
    private static final Logger logger = Logger.getLogger(BulkRenamerApp.class.getName());
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BulkRenamerApp.class.getResource("renamer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 860, 480);
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);

        RenamerController renamerController = fxmlLoader.getController();
        renamerController.setStageAndSetupListeners(stage);

        stage.setTitle("Bulk Renamer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}