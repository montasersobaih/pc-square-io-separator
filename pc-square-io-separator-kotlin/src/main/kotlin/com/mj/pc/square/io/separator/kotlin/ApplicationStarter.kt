package com.mj.pc.square.io.separator.kotlin

import com.mj.pc.square.io.separator.kotlin.constant.UInterface
import com.mj.pc.square.io.separator.kotlin.util.FXMLUtil
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 15-10-2022
 */

class ApplicationStarter : Application() {

    @Throws(Exception::class)
    override fun start(stage: Stage) {
        val pane = FXMLUtil.loadInterface(UInterface.APPLICATION_PAGE)
        stage.title = "PC^2 Test Case Separator"
        stage.scene = Scene(pane, 800.0, 500.0)
        stage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(ApplicationStarter::class.java, *args)
}