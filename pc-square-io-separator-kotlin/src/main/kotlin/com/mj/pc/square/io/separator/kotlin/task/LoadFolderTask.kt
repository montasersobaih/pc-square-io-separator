package com.mj.pc.square.io.separator.kotlin.task

import com.mj.pc.square.io.separator.kotlin.constant.Layout
import com.mj.pc.square.io.separator.kotlin.controller.layout.TestCaseLayoutController
import com.mj.pc.square.io.separator.kotlin.dialog.AlertDialog
import com.mj.pc.square.io.separator.kotlin.dialog.LoadingDialog
import com.mj.pc.square.io.separator.kotlin.exception.EmptyDirectoryException
import com.mj.pc.square.io.separator.kotlin.exception.LoadInputFileException
import com.mj.pc.square.io.separator.kotlin.exception.LoadOutputFileException
import com.mj.pc.square.io.separator.kotlin.exception.NoDirectorySelectedException
import com.mj.pc.square.io.separator.kotlin.exception.UnequalIOFilesNumberException
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
import javafx.stage.DirectoryChooser
import javafx.stage.Window
import java.io.File
import java.io.FileFilter
import java.nio.file.Files
import java.nio.file.Path
import java.util.LinkedList
import java.util.Objects
import java.util.Optional
import java.util.stream.Collectors

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 26-10-2022
 */

class LoadFolderTask(private val container: StackPane) : BaseTask<ObservableList<Node>>() {

    private val window: Window = container.scene.window

    private val loadingDialog: LoadingDialog = LoadingDialog(container)

    init {
        setEventHandler(WorkerStateEvent.ANY, TaskEvent())
    }

    @Throws(Exception::class)
    override fun call(): ObservableList<Node> {
        val directory = loadDirectoryChooser()
        Platform.runLater { loadingDialog.show() }
        com.mj.pc.square.io.separator.kotlin.ApplicationContext.directory = directory.parent

        val files = Files
            .list(directory)
            .map(Path::toFile)
            .filter(File::isFile)
            .filter { it.name.matches(".*\\.(in|out)".toRegex()) }
            .sorted()
            .collect(
                Collectors.groupingBy(
                    { it.name.substring(it.name.lastIndexOf(".") + 1) },
                    Collectors.mapping(File::toPath, Collectors.toCollection(::LinkedHashSet))
                )
            )

        val inputFiles = files["in"]!!
        val outputFiles = files["out"]!!
        if (inputFiles.size != outputFiles.size) {
            throw UnequalIOFilesNumberException()
        }

        val testCases: MutableList<TestCase> = LinkedList()

        val inputIT = inputFiles.iterator()
        val outputIT = outputFiles.iterator()
        while (inputIT.hasNext() && outputIT.hasNext()) {
            val input: String = try {
                Files.lines(inputIT.next())
                    .map(String::trim)
                    .filter(String::isNotEmpty)
                    .map { string ->
                        Optional
                            .of(string)
                            .filter { it.contains("#") }
                            .map { it.replace("#", "#\n") }
                            .orElse(string)
                    }
                    .collect(Collectors.joining("\n"))
            } catch (ex: Exception) {
                throw LoadInputFileException()
            }

            val output: String = try {
                Files.lines(outputIT.next())
                    .map(String::trim)
                    .filter(String::isNotEmpty)
                    .map { string ->
                        Optional
                            .of(string)
                            .filter { it.contains("#") }
                            .map { it.replace("#", "#\n") }
                            .orElse(string)
                    }
                    .collect(Collectors.joining("\n"))
            } catch (ex: Exception) {
                throw LoadOutputFileException()
            }

            testCases.add(TestCase(input, output))
        }

        val nodes = FXCollections.observableList(LinkedList<Node>())
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
    private fun loadDirectoryChooser(): Path {
        val chooser = DirectoryChooser()
        chooser.title = "Choose directory"
        chooser.initialDirectory = com.mj.pc.square.io.separator.kotlin.ApplicationContext.directory.toFile()

        val wrapper = com.mj.pc.square.io.separator.kotlin.util.SyncValue<File>()
        Platform.runLater { wrapper.value = chooser.showDialog(window) }

        val file = wrapper.value
        if (Objects.isNull(file) || !file!!.isDirectory) {
            throw NoDirectorySelectedException()
        } else {
            val filter = FileFilter { it.isFile && it.name.matches(".*\\.(in|out)".toRegex()) }
            if (Optional.of(filter).map(file::listFiles).filter { arr -> arr.isEmpty() }.isPresent) {
                throw EmptyDirectoryException()
            }
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