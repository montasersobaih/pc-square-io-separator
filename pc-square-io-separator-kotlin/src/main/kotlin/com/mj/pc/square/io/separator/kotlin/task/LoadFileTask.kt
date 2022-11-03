package com.mj.pc.square.io.separator.kotlin.task

import com.mj.pc.square.io.separator.kotlin.constant.Layout
import com.mj.pc.square.io.separator.kotlin.controller.layout.TestCaseLayoutController
import com.mj.pc.square.io.separator.kotlin.dialog.AlertDialog
import com.mj.pc.square.io.separator.kotlin.dialog.LoadingDialog
import com.mj.pc.square.io.separator.kotlin.exception.EmptyFileException
import com.mj.pc.square.io.separator.kotlin.exception.LoadFileException
import com.mj.pc.square.io.separator.kotlin.exception.NoFileSelectedException
import com.mj.pc.square.io.separator.kotlin.exception.UnsupportedFileException
import com.mj.pc.square.io.separator.kotlin.util.FXMLUtil
import com.mj.pc.square.io.separator.kotlin.util.TestCase
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Worker
import javafx.concurrent.WorkerStateEvent
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.layout.StackPane
import javafx.stage.FileChooser
import javafx.stage.Window
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.IOException
import java.nio.file.Path
import java.text.DecimalFormat
import java.util.LinkedList
import java.util.Objects
import java.util.Optional
import java.util.StringJoiner
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 26-10-2022
 */

class LoadFileTask(private val container: StackPane) : BaseTask<ObservableList<Node>>() {

    private val window: Window = container.scene.window

    private val loadingDialog: LoadingDialog = LoadingDialog(container)

    init {
        setEventHandler(WorkerStateEvent.ANY, TaskEvent())
    }

    @Throws(Exception::class)
    override fun call(): ObservableList<Node> {
        val filePath = loadFileChooser()
        Platform.runLater(loadingDialog::show)
        com.mj.pc.square.io.separator.kotlin.ApplicationContext.directory = filePath.parent

        // Parsing Excel sheets
        val stream = Stream.builder<String>()
        try {
            XSSFWorkbook(filePath.toFile()).use { workbook ->
                val decimal = DecimalFormat("#.#")
                for (sheet in workbook) { //Loading excel sheets
                    for (i in 1..sheet.lastRowNum) {
                        val testCase = StringJoiner("\n-\n")

                        for (cell in sheet.getRow(i)) {
                            when (cell.cellType) {
                                CellType.NUMERIC -> {
                                    val number = cell.numericCellValue
                                    testCase.add(decimal.format(number))
                                }

                                CellType.STRING -> testCase.add(cell.stringCellValue)
                                else -> {}
                            }
                        }

                        stream.accept(testCase.toString())
                    }
                }
            }
        } catch (ex: IOException) {
            throw LoadFileException()
        }

        val nodes = FXCollections.observableList(LinkedList<Node>())
        val testCases = stream.build().map(TestCase::parse).collect(Collectors.toList())

        for (i in testCases.indices) {
            val testCase = testCases[i]

            val testNumber = String.format("Test %03d", i + 1)

            val testCaseInterface = FXMLUtil.getFXMLLoader(Layout.TEST_CASE_LAYOUT)
            val testCasePane = FXMLUtil.loadInterface(testCaseInterface)
            val controller = testCaseInterface!!.getController<TestCaseLayoutController>()

            controller.setTitle(testNumber)
            controller.setTestCase(testCase)
            testCasePane!!.userData = testCase

            nodes.add(testCasePane)
        }

        return nodes
    }

    @Throws(Exception::class)
    private fun loadFileChooser(): Path {
        val chooser = FileChooser()
        chooser.title = "Choose File"
        chooser.initialDirectory = com.mj.pc.square.io.separator.kotlin.ApplicationContext.directory.toFile()
        chooser.extensionFilters.setAll(FileChooser.ExtensionFilter("Excel files", "*.xls", "*.xlsx"))

        val wrapper = com.mj.pc.square.io.separator.kotlin.util.SyncValue<File?>()

        Platform.runLater { wrapper.value = chooser.showOpenDialog(window) }

        val file: File? = wrapper.value
        if (Objects.isNull(file)) {
            throw NoFileSelectedException()
        } else if (!file!!.isFile) {
            throw UnsupportedFileException()
        } else if (file.length() == 0L) {
            throw EmptyFileException()
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
                        .map { obj: Worker<Any?> -> obj.exception }
                        .map { obj: Throwable -> obj.message }
                        .ifPresent { AlertDialog.show(container, it) }
                    loadingDialog.close()
                }

                Worker.State.CANCELLED -> loadingDialog.close()
                Worker.State.SUCCEEDED -> loadingDialog.close()
            }
        }
    }
}