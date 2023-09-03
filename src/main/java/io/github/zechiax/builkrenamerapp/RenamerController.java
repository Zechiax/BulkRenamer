package io.github.zechiax.builkrenamerapp;

import io.github.zechiax.builkrenamerapp.core.FileToRename;
import io.github.zechiax.builkrenamerapp.core.Helpers.FileSizeConverter;
import io.github.zechiax.builkrenamerapp.core.RenameManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static java.lang.System.Logger.Level.*;

public class RenamerController {
    private final RenameManager fileManager;
    private final System.Logger logger = System.getLogger(RenamerController.class.getName());
    @FXML
    public Button addFilesButton;
    private final ObservableList<FileToRename> selectedFiles = FXCollections.observableArrayList();
    @FXML
    public Button removeSelectedButton;
    @FXML
    public Button clearAllButton;
    @FXML
    public TextField newNameTextField;
    @FXML
    public TableView<FileToRename> selectedFilesTableView = new TableView<>();
    @FXML
    public TableColumn<FileToRename, String> oldNameColumn = new TableColumn<>();
    @FXML
    public TableColumn<FileToRename, String> newNameColumn = new TableColumn<>();
    @FXML
    public TableColumn<FileToRename, String> fileSizeColumn = new TableColumn<>();
    public TableColumn<FileToRename, String> dateModifiedColumn = new TableColumn<>();
    @FXML
    public TableColumn<FileToRename, String> pathColumn = new TableColumn<>();
    @FXML
    public TableColumn<FileToRename, String> indexColumn = new TableColumn<>();
    @FXML
    public TextField firstNumberTextField;
    @FXML
    public TextField stepTextField;
    @FXML
    public TextField paddingTextField;
    @FXML
    public Button moveUpButton;
    @FXML
    public Button moveDownButton;
    @FXML
    public TextField newExtensionTextField;
    @FXML
    public Button renameButton;
    @FXML
    private Stage stage;

    public RenamerController() {
        this.fileManager = new RenameManager(this.selectedFiles);
        updateTableView();
    }

    private String getNewName() {
        var newName = newNameTextField.getText();
        var extension = newExtensionTextField.getText();

        return newName + "." + extension;
    }

    private void updateTableView() {
        selectedFilesTableView.setItems(selectedFiles);
        selectedFilesTableView.columnResizePolicyProperty().set(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);

        oldNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        newNameColumn.setCellValueFactory(cellData -> {
            var file = cellData.getValue();
            var newName = fileManager.getRenamedFile(getNewName(), file);
            return new SimpleStringProperty(newName);
        });
        fileSizeColumn.setCellValueFactory(cellData -> {
            var file = cellData.getValue();
            var size = file.length();

            var text = FileSizeConverter.getFormattedSizeFromBytes(size);

            return new SimpleStringProperty(text);
        });
        dateModifiedColumn.setCellValueFactory(cellData -> {
            var file = cellData.getValue();

            try {
                var date = file.getFileAttributeView().lastModifiedTime().toInstant();
                var text = DateTimeFormatter.ISO_INSTANT.format(date);

                return new SimpleStringProperty(text);
            } catch (IOException e) {
                logger.log(ERROR, "Error getting last modified time", e);
                return new SimpleStringProperty("Error");
            }
        });
        pathColumn.setCellValueFactory(cellData -> {
            var file = cellData.getValue();
            var path = file.getAbsolutePath();
            return new SimpleStringProperty(path);
        });
        indexColumn.setCellValueFactory(cellData -> {
            var file = cellData.getValue();
            var index = selectedFiles.indexOf(file) + 1;
            return new SimpleStringProperty(Integer.toString(index));
        });

        // Automatically resize the index column
        //indexColumn.prefWidthProperty().bind(selectedFilesTableView.widthProperty().multiply(0.05));

        selectedFilesTableView.refresh();
    }

    @FXML protected void onAddFilesButtonClick() {
        logger.log(INFO, "Add originalFiles button clicked");

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select files to rename");
        var readonlyFiles = fileChooser.showOpenMultipleDialog(stage);

        if (readonlyFiles == null) {
            logger.log(INFO, "No originalFiles selected");
            return;
        }

        var files = new ArrayList<>(readonlyFiles);

        // We remove the files that are already in the list
        files.removeIf(file -> {
            for (var selectedFile : selectedFiles) {
                if (selectedFile.getAbsolutePath().equals(file.getAbsolutePath())) {
                    return true;
                }
            }
            return false;
        });

        logger.log(INFO, "New files selected: " + files.size());
        this.selectedFiles.addAll(FileToRename.convertToFilesToRename(files));
        logger.log(DEBUG, "Files added to list");


        updateTableView();
    }

    private void onNewSelectedFilesChange(ListChangeListener.Change<? extends File> change) {
        logger.log(INFO, "Selected originalFiles changed");
    }

    private void onRemoveSelectedButtonClick(ActionEvent actionEvent) {
        logger.log(INFO, "Remove selected button clicked");
        var selectedItems = this.selectedFilesTableView.getSelectionModel().getSelectedItems();
        logger.log(DEBUG, "Selected items: " + selectedItems.size());

        this.selectedFiles.removeAll(selectedItems);
        logger.log(DEBUG, "Selected items removed from list");

        // We clear the selection
        this.selectedFilesTableView.getSelectionModel().clearSelection();

        updateTableView();
    }

    private void onClearAllButtonClick(ActionEvent actionEvent) {
        logger.log(INFO, "Clear all button clicked");
        this.selectedFiles.clear();

        updateTableView();
    }

    private void counterTextFieldChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (oldValue.equals(newValue)) {
            // No change
            return;
        }

        if (newValue.isEmpty()) {
            // Empty string, user is probably deleting the text to input a new value
            return;
        }

        var start = Integer.parseInt(firstNumberTextField.getText());
        var step = Integer.parseInt(stepTextField.getText());
        var padding = Integer.parseInt(paddingTextField.getText());

        fileManager.setCounterSettings(start, step, padding);

        updateTableView();
    }

    private void onTextFieldChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (oldValue.equals(newValue)) {
            // No change
            return;
        }

        updateTableView();
    }

    private void onMoveDownButtonClick(ActionEvent e) {
        // We move the selected item down by one position
        var selectedItem = this.selectedFilesTableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        var index = this.selectedFiles.indexOf(selectedItem);

        if (index == this.selectedFiles.size() - 1) {
            // Already at the bottom
            return;
        }

        this.selectedFiles.remove(selectedItem);
        this.selectedFiles.add(index + 1, selectedItem);
        this.selectedFilesTableView.getSelectionModel().clearSelection();
        this.selectedFilesTableView.getSelectionModel().select(selectedItem);

        updateTableView();
    }

    private void onMoveUpButtonClick(ActionEvent e) {
        // We move the selected item up by one position
        var selectedItem = this.selectedFilesTableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        var index = this.selectedFiles.indexOf(selectedItem);

        if (index == 0) {
            // Already at the top
            return;
        }

        this.selectedFiles.remove(selectedItem);
        this.selectedFiles.add(index - 1, selectedItem);
        this.selectedFilesTableView.getSelectionModel().clearSelection();
        this.selectedFilesTableView.getSelectionModel().select(selectedItem);

        updateTableView();
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
        // Turn on multiple selection
        this.selectedFilesTableView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        this.newNameTextField.textProperty().addListener(this::onTextFieldChange);
        this.newExtensionTextField.textProperty().addListener(this::onTextFieldChange);

        var counterSettings = fileManager.getCounterSettings();

        this.firstNumberTextField.setText(Integer.toString(counterSettings.getStart()));
        this.stepTextField.setText(Integer.toString(counterSettings.getStep()));
        this.paddingTextField.setText(Integer.toString(counterSettings.getPadding()));

        // Counter settings
        this.firstNumberTextField.textProperty().addListener(this::counterTextFieldChange);
        this.stepTextField.textProperty().addListener(this::counterTextFieldChange);
        this.paddingTextField.textProperty().addListener(this::counterTextFieldChange);

        // Move buttons
        this.moveUpButton.setOnAction(this::onMoveUpButtonClick);
        this.moveDownButton.setOnAction(this::onMoveDownButtonClick);

        // Rename button
        this.renameButton.setOnAction(this::onRenameButtonClick);
    }

    private void onRenameButtonClick(ActionEvent actionEvent) {
        var mask = getNewName();

        try {
            fileManager.renameFiles(mask);
        } catch (IOException e) {
            logger.log(ERROR, "Error renaming files", e);
            // We show error dialog
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error renaming files");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        updateTableView();
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        var count = selectedFiles.size();
        alert.setHeaderText("Successfully renamed " + count + " files");
        alert.setContentText("The files have been renamed successfully");
        alert.showAndWait();
    }
}
