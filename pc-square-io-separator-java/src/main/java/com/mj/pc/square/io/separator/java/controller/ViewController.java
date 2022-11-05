package com.mj.pc.square.io.separator.java.controller;

import com.mj.pc.square.io.separator.java.dialog.AlertDialog;
import com.mj.pc.square.io.separator.java.dialog.ChooseDialog;
import com.mj.pc.square.io.separator.java.task.ExportToExcelSheetTask;
import com.mj.pc.square.io.separator.java.task.ExportToIOFilesTask;
import com.mj.pc.square.io.separator.java.task.LoadFileTask;
import com.mj.pc.square.io.separator.java.task.LoadFolderTask;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 15-10-2022
 */

public final class ViewController extends BaseController {

    @FXML
    private StackPane viewPane;

    @FXML
    private Button exportationButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewPane.sceneProperty().addListener((i1, i2, scene) -> {
            scene.windowProperty().addListener((i3, i4, window) -> {
                window.setOnCloseRequest(ignored -> executor.shutdown());
            });
        });
        exportationButton.disableProperty().bind(Bindings.isEmpty(data.getChildren()));
    }

    @FXML
    private void onMenuItemAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();

        switch (menuItem.getId()) {
            case "import-file":
                LoadFileTask loadFileTask = new LoadFileTask(viewPane);
                loadFileTask.setOnSucceeded(this::onTaskSucceeded);
                executor.schedule(loadFileTask, SCHEDULE_DELAY, DELAY_TIMEUNIT);
                break;
            case "import-folder":
                LoadFolderTask loadFolderTask = new LoadFolderTask(viewPane);
                loadFolderTask.setOnSucceeded(this::onTaskSucceeded);
                executor.schedule(loadFolderTask, SCHEDULE_DELAY, DELAY_TIMEUNIT);
                break;
            case "close":
            case "about":
                String messageKey = "message.alert.under.development";
                AlertDialog.show(viewPane, messageKey);
                break;
        }
    }

    @FXML
    private void dataExportationButtonAction(ActionEvent event) {
        List<Node> data = this.data.getChildren();

        if (data.isEmpty()) {
            String messageKey = "message.alert.data.exportation.empty";
            AlertDialog.show(viewPane, getString(messageKey));
        } else {
            ChooseDialog.builder(viewPane)
                    .setOnRadioButtonClicked(this::onRadioButtonClicked)
                    .build()
                    .show();
        }
    }

    private void onRadioButtonClicked(MouseEvent event) {
        RadioButton radio = (RadioButton) event.getSource();
        List<Node> nodes = data.getChildren();

        Task<Void> task = null;
        switch (radio.getId()) {
            case "single-file":
                task = new ExportToExcelSheetTask(viewPane, nodes);
                break;
            case "multiple-files":
                task = new ExportToIOFilesTask(viewPane, nodes);
                break;
        }

        assert Objects.nonNull(task);
        task.setOnSucceeded(this::onTaskSucceeded);
        executor.schedule(task, SCHEDULE_DELAY, DELAY_TIMEUNIT);
    }

    @SuppressWarnings("unchecked")
    private void onTaskSucceeded(WorkerStateEvent event) {
        Worker<?> worker = event.getSource();

        if (worker instanceof ExportToExcelSheetTask || worker instanceof ExportToIOFilesTask) {
            data.getChildren().clear();

            String messageKey = "message.alert.exportation.success";
            AlertDialog.show(viewPane, getString(messageKey));
        } else if (worker instanceof LoadFolderTask || worker instanceof LoadFileTask) {
            ObservableList<Node> nodes = (ObservableList<Node>) worker.getValue();
            data.getChildren().setAll(nodes);
        }
    }
}
