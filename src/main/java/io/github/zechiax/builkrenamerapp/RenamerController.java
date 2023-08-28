package io.github.zechiax.builkrenamerapp;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;

public class RenamerController {
    private final System.Logger logger = System.getLogger(RenamerController.class.getName());
    @FXML
    public Button addFilesButton;
    @FXML
    public ListView<File> selectedFilesListView;
    private final ObservableList<File> newSelectedFiles = FXCollections.observableArrayList();
    private Stage stage;

    @FXML protected void onAddFilesButtonClick() {
        logger.log(INFO, "Add files button clicked");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select files to rename");

        var files = fileChooser.showOpenMultipleDialog(stage);

        if (files == null) {
            logger.log(INFO, "No files selected");
            return;
        }

        logger.log(INFO, "Files selected: " + files.size());
        this.newSelectedFiles.addAll(files);
    }

    private void onNewSelectedFilesChange(ListChangeListener.Change<? extends File> change) {
        logger.log(INFO, "New files selected: " + newSelectedFiles.size());

        this.selectedFilesListView.setItems(newSelectedFiles);

        this.newSelectedFiles.clear();
    }

    public void setStageAndSetupListeners(Stage stage) {
        stage.setOnCloseRequest(event -> {
            logger.log(INFO, "Stage closed");
            Platform.exit();
        });

        this.stage = stage;

        this.addFilesButton.setOnAction(t -> onAddFilesButtonClick());

        this.newSelectedFiles.addListener(this::onNewSelectedFilesChange);
    }
}
