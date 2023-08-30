package io.github.zechiax.builkrenamerapp;

import io.github.zechiax.builkrenamerapp.core.FileToRename;
import io.github.zechiax.builkrenamerapp.core.RenameManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;

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
    private Stage stage;

    public RenamerController() {
        this.fileManager = new RenameManager(this.selectedFiles);
        updateTableView();
    }

    private void updateTableView() {
        selectedFilesTableView.setItems(selectedFiles);
        selectedFilesTableView.columnResizePolicyProperty().set(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);

        oldNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        newNameColumn.setCellValueFactory(cellData -> {
            var file = cellData.getValue();
            var newName = fileManager.getRenamedFile(newNameTextField.getText(), file);
            return new SimpleStringProperty(newName);
        });
        fileSizeColumn.setCellValueFactory(cellData -> {
            var file = cellData.getValue();
            var size = file.length();
            return new SimpleStringProperty(Long.toString(size));
        });
        dateModifiedColumn.setCellValueFactory(cellData -> {
            var file = cellData.getValue();
            var date = file.lastModified();
            return new SimpleStringProperty(Long.toString(date));
        });
        pathColumn.setCellValueFactory(cellData -> {
            var file = cellData.getValue();
            var path = file.getAbsolutePath();
            return new SimpleStringProperty(path);
        });

        selectedFilesTableView.refresh();
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

    private void onTextFieldChange() {
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

        this.newNameTextField.textProperty().addListener((observable, oldValue, newValue) -> onTextFieldChange());
    }
}
