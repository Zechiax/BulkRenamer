package io.github.zechiax.builkrenamerapp;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckListView;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.zechiax.builkrenamerapp.core.RenameManager;
import io.github.zechiax.builkrenamerapp.core.FileToRename;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;

public class RenamerController {
    private final RenameManager fileManager;
    private final System.Logger logger = System.getLogger(RenamerController.class.getName());
    @FXML
    public MFXButton addFilesButton;
    @FXML
    public MFXListView<FileToRename> selectedFilesListView;
    private final ObservableList<FileToRename> selectedFiles = FXCollections.observableArrayList();
    @FXML
    public MFXButton removeSelectedButton;
    @FXML
    public MFXButton clearAllButton;
    @FXML
    public MFXListView<String> renamedFilesListView;
    @FXML
    public MFXTextField newNameTextField;
    private Stage stage;

    public RenamerController() {
        this.fileManager = new RenameManager(this.selectedFiles);
    }

    @FXML protected void onAddFilesButtonClick() {
        logger.log(INFO, "Add originalFiles button clicked");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select originalFiles to rename");

        var files = fileChooser.showOpenMultipleDialog(stage);

        if (files == null) {
            logger.log(INFO, "No originalFiles selected");
            return;
        }

        logger.log(INFO, "Files selected: " + files.size());
        this.selectedFiles.addAll(FileToRename.convertToFilesToRename(files));

        updateRenamedFilesListView();
    }

    private void onNewSelectedFilesChange(ListChangeListener.Change<? extends File> change) {
        logger.log(INFO, "Selected originalFiles changed");
    }

    private void onRemoveSelectedButtonClick(ActionEvent actionEvent) {
        logger.log(INFO, "Remove selected button clicked");
        var selectedItems = this.selectedFilesListView.getSelectionModel().getSelection();
        logger.log(DEBUG, "Selected items: " + selectedItems.size());

        // We map observable map <Integer, FileToRename> to ArrayList<FileToRename>
        // and then we remove all selected items from the list
        var selectedItemsList = selectedItems.values();
        this.selectedFiles.removeAll(selectedItemsList);
        logger.log(DEBUG, "Selected items removed from list");

        // We clear the selection
        this.selectedFilesListView.getSelectionModel().clearSelection();

        updateRenamedFilesListView();
    }

    private void onClearAllButtonClick(ActionEvent actionEvent) {
        logger.log(INFO, "Clear all button clicked");
        this.selectedFiles.clear();

        updateRenamedFilesListView();
    }

    private void onTextFieldChange() {
        updateRenamedFilesListView();
    }

    private void updateRenamedFilesListView() {
        var newName = this.newNameTextField.getText();
        logger.log(DEBUG, "New name: " + newName);
        var renamedFiles = this.fileManager.GetRenamedFiles(newName);
        this.renamedFilesListView.setItems(FXCollections.observableArrayList(renamedFiles));
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
        this.selectedFilesListView.getSelectionModel().setAllowsMultipleSelection(true);

        this.newNameTextField.textProperty().addListener((observable, oldValue, newValue) -> onTextFieldChange());
    }
}
