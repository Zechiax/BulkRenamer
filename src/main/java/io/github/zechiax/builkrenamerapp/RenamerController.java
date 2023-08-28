package io.github.zechiax.builkrenamerapp;

import io.github.zechiax.builkrenamerapp.core.FileManager;
import io.github.zechiax.builkrenamerapp.core.FileToRename;
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
    private final FileManager fileManager = new FileManager();
    private final System.Logger logger = System.getLogger(RenamerController.class.getName());
    @FXML
    public Button addFilesButton;
    @FXML
    public ListView<FileToRename> selectedFilesListView;
    private final ObservableList<FileToRename> selectedFiles = FXCollections.observableArrayList();
    @FXML
    public Button removeSelectedButton;
    @FXML
    public Button clearAllButton;
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
        this.selectedFiles.addAll(FileToRename.convertToFilesToRename(files));
    }

    private void onNewSelectedFilesChange(ListChangeListener.Change<? extends File> change) {
        logger.log(INFO, "Selected files changed");
    }

    private void onRemoveSelectedButtonClick(ActionEvent actionEvent) {
        logger.log(INFO, "Remove selected button clicked");
        var selectedItems = this.selectedFilesListView.getSelectionModel().getSelectedItems();
        logger.log(DEBUG, "Selected items: " + selectedItems.size());
        this.selectedFiles.removeAll(selectedItems);
    }

    private void onClearAllButtonClick(ActionEvent actionEvent) {
        logger.log(INFO, "Clear all button clicked");
        this.selectedFiles.clear();
    }

    public void setStageAndSetupListeners(Stage stage) {
        stage.setOnCloseRequest(event -> {
            logger.log(INFO, "Stage closed");
            Platform.exit();
        });

        this.stage = stage;

        this.addFilesButton.setOnAction(t -> onAddFilesButtonClick());
        this.removeSelectedButton.setOnAction(this::onRemoveSelectedButtonClick);
        this.clearAllButton.setOnAction(this::onClearAllButtonClick);

        this.selectedFiles.addListener(this::onNewSelectedFilesChange);
        this.selectedFilesListView.setItems(selectedFiles);
        // Turn on multiple selection
        this.selectedFilesListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

    }
}
