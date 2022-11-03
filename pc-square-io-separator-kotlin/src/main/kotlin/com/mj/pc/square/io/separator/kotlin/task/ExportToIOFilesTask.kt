package com.mj.pc.square.io.separator.kotlin.task

import com.mj.pc.square.io.separator.kotlin.dialog.AlertDialog
import com.mj.pc.square.io.separator.kotlin.dialog.LoadingDialog
import com.mj.pc.square.io.separator.kotlin.exception.NoDirectorySelectedException
import com.mj.pc.square.io.separator.kotlin.exception.WriteFileException
import com.mj.pc.square.io.separator.kotlin.util.NodeUtil
import com.mj.pc.square.io.separator.kotlin.util.TestCase
import javafx.application.Platform
import javafx.concurrent.Worker
import javafx.concurrent.WorkerStateEvent
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.layout.StackPane
import javafx.stage.DirectoryChooser
import javafx.stage.Window
import java.io.File
import java.nio.charset.StandardCharsets
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

class ExportToIOFilesTask(private val container: StackPane, private val nodes: List<Node>) : BaseTask<Void?>() {

    private val window: Window = container.scene.window

    private val loadingDialog: LoadingDialog = LoadingDialog(container)

    init {
        setEventHandler(WorkerStateEvent.ANY, TaskEvent())
    }

    @Throws(Exception::class)
    override fun call(): Void? {
        val directory = loadDirectoryChooser()
        Platform.runLater(loadingDialog::show)

        val testCases: List<TestCase> = NodeUtil.extractUserData(nodes)
        try {
            for (i in testCases.indices) {
                val testCase = testCases[i]
                val filePath = String.format("%s/Test%03d", directory, i + 1)
                Files.write(directory.resolve("$filePath.in"), testCase.input.toByteArray(StandardCharsets.UTF_8))
                Files.write(directory.resolve("$filePath.out"), testCase.output.toByteArray(StandardCharsets.UTF_8))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw WriteFileException(ex.message!!)
        }

        return null
    }

    @Throws(Exception::class)
    private fun loadDirectoryChooser(): Path {
        val chooser = DirectoryChooser()
        chooser.title = "Choose folder"
        chooser.title = "Choose folder"
        chooser.initialDirectory = com.mj.pc.square.io.separator.kotlin.ApplicationContext.directory.toFile()

        val wrapper = com.mj.pc.square.io.separator.kotlin.util.SyncValue<File>()
        Platform.runLater { wrapper.value = chooser.showDialog(window) }

        val file = wrapper.value
        if (Objects.isNull(file) || !file!!.isDirectory) {
            throw NoDirectorySelectedException()
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