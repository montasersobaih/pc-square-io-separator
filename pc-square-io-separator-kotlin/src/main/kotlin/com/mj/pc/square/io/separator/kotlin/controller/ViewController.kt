package com.mj.pc.square.io.separator.kotlin.controller

import com.mj.pc.square.io.separator.kotlin.dialog.AlertDialog
import com.mj.pc.square.io.separator.kotlin.dialog.ChooseDialog
import com.mj.pc.square.io.separator.kotlin.task.ExportToExcelSheetTask
import com.mj.pc.square.io.separator.kotlin.task.ExportToIOFilesTask
import com.mj.pc.square.io.separator.kotlin.task.LoadFileTask
import com.mj.pc.square.io.separator.kotlin.task.LoadFolderTask
import javafx.beans.binding.Bindings
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.concurrent.WorkerStateEvent
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.MenuItem
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.net.URL
import java.util.ResourceBundle

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 15-10-2022
 */

class ViewController : BaseController() {

    @FXML
    private lateinit var viewPane: StackPane

    @FXML
    private lateinit var exportationButton: Button

    @FXML
    private lateinit var scrollPane: ScrollPane

    @FXML
    private lateinit var data: VBox

    override fun initialize(location: URL, resources: ResourceBundle) {
        viewPane.sceneProperty().addListener { _, _, scene ->
            scene.windowProperty().addListener { _, _, window ->
                window.setOnCloseRequest { executor.shutdown() }
            }
        }
        exportationButton.disableProperty().bind(Bindings.isEmpty(data.children))
    }

    @FXML
    private fun onMenuItemAction(event: ActionEvent) {
        val menuItem = event.source as MenuItem
        when (menuItem.id) {
            "import-file" -> {
                val loadFileTask = LoadFileTask(viewPane)
                loadFileTask.setOnSucceeded(::onTaskExecutionEvent)
                executor.schedule(loadFileTask, SCHEDULE_DELAY, DELAY_TIMEUNIT)
            }

            "import-folder" -> {
                val loadFolderTask = LoadFolderTask(viewPane)
                loadFolderTask.setOnSucceeded(::onTaskExecutionEvent)
                executor.schedule(loadFolderTask, SCHEDULE_DELAY, DELAY_TIMEUNIT)
            }

            "close", "about" -> {
                val messageKey = "message.alert.under.development"
                AlertDialog.show(viewPane, getString(messageKey))
            }
        }
    }

    @FXML
    private fun dataExportationButtonAction(event: ActionEvent) {
        val data: List<Node> = data.children
        if (data.isEmpty()) {
            val messageKey = "message.alert.data.exportation.empty"
            AlertDialog.show(viewPane, getString(messageKey))
        } else {
            ChooseDialog.builder(viewPane)
                .setOnRadioButtonClicked(::onRadioButtonClicked)
                .build()
                .show()
        }
    }

    private fun onRadioButtonClicked(event: MouseEvent) {
        val radio = event.source as RadioButton
        val nodes: List<Node> = data.children

        var task: Task<Void?>? = null
        when (radio.id) {
            "single-file" -> task = ExportToExcelSheetTask(viewPane, nodes)
            "multiple-files" -> task = ExportToIOFilesTask(viewPane, nodes)
        }

        task!!.setOnSucceeded(::onTaskExecutionEvent)
        executor.schedule(task, SCHEDULE_DELAY, DELAY_TIMEUNIT)
    }

    @Suppress("UNCHECKED_CAST")
    private fun onTaskExecutionEvent(event: WorkerStateEvent) {
        when (val worker = event.source) {
            is ExportToExcelSheetTask, is ExportToIOFilesTask -> {
                val messageKey = getString("message.alert.exportation.success");
                AlertDialog.show(viewPane, getString(messageKey)).also { data.children.clear() }
            }

            is LoadFileTask, is LoadFolderTask -> {
                (worker.value as ObservableList<Node>).also(data.children::setAll)
            }
        }
    }
}