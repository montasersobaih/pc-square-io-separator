package com.mj.pc.square.io.separator.kotlin.task

import com.mj.pc.square.io.separator.kotlin.dialog.AlertDialog
import com.mj.pc.square.io.separator.kotlin.dialog.LoadingDialog
import com.mj.pc.square.io.separator.kotlin.exception.NoFileSelectedException
import com.mj.pc.square.io.separator.kotlin.exception.UnsupportedFileException
import com.mj.pc.square.io.separator.kotlin.exception.WriteFileException
import com.mj.pc.square.io.separator.kotlin.util.NodeUtil
import com.mj.pc.square.io.separator.kotlin.util.TestCase
import javafx.application.Platform
import javafx.concurrent.Worker
import javafx.concurrent.WorkerStateEvent
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.layout.StackPane
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Window
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.util.CellUtil
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.Objects
import java.util.Optional

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 24-10-2022
 */

class ExportToExcelSheetTask(private val container: StackPane, private val nodes: List<Node>) : BaseTask<Void?>() {

    private val window: Window = container.scene.window

    private val loadingDialog: LoadingDialog = LoadingDialog(container)

    init {
        setEventHandler(WorkerStateEvent.ANY, TaskEvent())
    }

    @Throws(Exception::class)
    override fun call(): Void? {
        val file: Path = loadFileChooser()
        Platform.runLater(loadingDialog::show)

        val testCases: List<TestCase> = NodeUtil.extractUserData(nodes)
        try {
            Files.newOutputStream(file).use { output ->
                val workbook = XSSFWorkbook()

                val cellStyle = workbook.createCellStyle()
                cellStyle.alignment = HorizontalAlignment.LEFT
                cellStyle.verticalAlignment = VerticalAlignment.TOP

                val sheet: Sheet = workbook.createSheet("Sheet 1")

                var row = sheet.createRow(0)
                val headers = arrayOf("INPUT", "OUTPUT")
                for (i in headers.indices) {
                    CellUtil.createCell(row, i, headers[i], cellStyle)
                }

                for (i in testCases.indices) {
                    val testCase = testCases[i]

                    row = sheet.createRow(i + 1)
                    val values = arrayOf(testCase.input, testCase.output)
                    for (j in values.indices) {
                        CellUtil.createCell(row, j, values[j], cellStyle)
                    }
                }

                workbook.write(output)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw WriteFileException(ex.message!!)
        }

        return null
    }

    @Throws(Exception::class)
    private fun loadFileChooser(): Path {
        val chooser = FileChooser()
        chooser.title = "Choose File"
        chooser.initialDirectory = com.mj.pc.square.io.separator.kotlin.ApplicationContext.directory.toFile()
        chooser.extensionFilters.setAll(ExtensionFilter("Excel files", "*.xls", "*.xlsx"))

        val wrapper = com.mj.pc.square.io.separator.kotlin.util.SyncValue<File>()

        Platform.runLater { wrapper.value = chooser.showOpenDialog(window) }

        val file = wrapper.value
        if (Objects.isNull(file)) {
            throw NoFileSelectedException()
        } else if (!file!!.isFile) {
            throw UnsupportedFileException()
        }

        return file.toPath()
    }

    /*================================================={Inner classes}================================================*/
    private inner class TaskEvent : EventHandler<WorkerStateEvent> {

        @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
        override fun handle(event: WorkerStateEvent) {
            val worker = event.source

            when (worker.state) {
                Worker.State.READY -> {}
                Worker.State.SCHEDULED -> {}
                Worker.State.RUNNING -> {}
                Worker.State.FAILED -> {
                    Optional.of(worker)
                        .map { it.exception }
                        .map { it.message }
                        .ifPresent { AlertDialog.show(container, it) }
                    loadingDialog.close()
                }

                Worker.State.CANCELLED -> loadingDialog.close()
                Worker.State.SUCCEEDED -> loadingDialog.close()
            }
        }
    }
}