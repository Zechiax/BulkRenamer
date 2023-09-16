package io.github.zechiax.app;

import io.github.zechiax.api.FileToRename;
import io.github.zechiax.app.core.Helpers.FileSizeConverter;
import io.github.zechiax.app.core.RenameManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class RenamerController {
    private final RenameManager renameManager;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(RenamerController.class);
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
    public TextField findTextField;
    @FXML
    public TextField replaceTextField;
    @FXML
    private Stage stage;

    public RenamerController() {
        this.renameManager = new RenameManager(this.selectedFiles);
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
            var newName = renameManager.getRenamedFile(getNewName(), file);
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
                logger.error("Error getting last modified time", e);
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

        selectedFilesTableView.refresh();
    }

    @FXML protected void onAddFilesButtonClick() {


        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select files to rename");
        var readonlyFiles = fileChooser.showOpenMultipleDialog(stage);

        if (readonlyFiles == null) {
            return;
        }

        var files = new ArrayList<>(readonlyFiles);

        newSelectedFiles(files);
    }

    private void newSelectedFiles(ArrayList<File> files) {
        // We remove the files that are already in the list
        files.removeIf(file -> {
            for (var selectedFile : selectedFiles) {
                if (selectedFile.getAbsolutePath().equals(file.getAbsolutePath())) {
                    return true;
                }
            }
            return false;
        });

        this.selectedFiles.addAll(FileToRename.convertToFilesToRename(files));

        updateTableView();
    }

    private void onNewSelectedFilesChange(ListChangeListener.Change<? extends File> change) {
        logger.info("Selected originalFiles changed");
    }

    private void onRemoveSelectedButtonClick(ActionEvent actionEvent) {
        var selectedItems = this.selectedFilesTableView.getSelectionModel().getSelectedItems();

        this.selectedFiles.removeAll(selectedItems);

        // We clear the selection
        this.selectedFilesTableView.getSelectionModel().clearSelection();

        updateTableView();
    }

    private void onClearAllButtonClick(ActionEvent actionEvent) {
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

        renameManager.setCounterSettings(start, step, padding);

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

    private void onFindReplaceChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (oldValue.equals(newValue)) {
            // No change
            return;
        }

        renameManager.setFindReplace(findTextField.getText(), replaceTextField.getText());

        updateTableView();
    }

    public void setStageAndSetupListeners(Stage stage) {
        stage.setOnCloseRequest(event -> {
            logger.info("Stage closed");
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

        var counterSettings = renameManager.getCounterSettings();

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

        // Find and replace
        this.findTextField.textProperty().addListener(this::onFindReplaceChanged);
        this.replaceTextField.textProperty().addListener(this::onFindReplaceChanged);
    }

    private void onRenameButtonClick(ActionEvent actionEvent) {
        var mask = getNewName();

        try {
            renameManager.renameFiles(mask);
        } catch (IOException e) {
            logger.error("Error renaming files", e);
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
