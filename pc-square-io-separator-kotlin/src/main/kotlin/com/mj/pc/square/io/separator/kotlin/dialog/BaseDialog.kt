package com.mj.pc.square.io.separator.kotlin.dialog

import com.jfoenix.controls.JFXDialog
import com.jfoenix.controls.events.JFXDialogEvent
import javafx.fxml.Initializable
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import java.net.URL
import java.util.ResourceBundle

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 15-10-2022
 */

abstract class BaseDialog<R : Pane> protected constructor(container: StackPane) : JFXDialog(), Initializable {

    private var resources: ResourceBundle? = null

    init {
        super.setDialogContainer(container)
        super.setOverlayClose(false)
        super.setContent(@Suppress("LeakingThis") initializeLayout())
        super.setEventHandler(KeyEvent.ANY, ::onDialogKeyPressed)
        super.addEventHandler(JFXDialogEvent.OPENED) { requestFocus() }
    }

    override fun initialize(location: URL, resources: ResourceBundle) {
        this.resources = resources
    }

    protected abstract fun initializeLayout(): R?

    protected fun setParentBackground(@Suppress("SameParameterValue") color: Color) {
        val pane = content.parent as Pane
        pane.background = Background(BackgroundFill(color, null, null))
    }

    protected fun getString(string: String): String {
        return resources!!.getString(string)
    }

    protected open fun onDialogKeyPressed(event: KeyEvent) {
        if (event.code == KeyCode.ESCAPE) {
            close()
        }
    }
}