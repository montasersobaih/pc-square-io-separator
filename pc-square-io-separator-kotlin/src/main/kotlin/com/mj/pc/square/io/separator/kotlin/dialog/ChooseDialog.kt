package com.mj.pc.square.io.separator.kotlin.dialog

import com.mj.pc.square.io.separator.kotlin.constant.DInterface
import com.mj.pc.square.io.separator.kotlin.util.FXMLUtil
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import java.util.Optional
import java.util.stream.Stream

/**
 * @author Montaser Sobaih
 * @version 1.0
 * @since 13-06-2021
 */

class ChooseDialog private constructor(container: StackPane) : BaseDialog<BorderPane>(container) {

    @FXML
    private lateinit var choosePane: BorderPane

    private var eventHandler: EventHandler<MouseEvent>? = null

    init {
        super.setOverlayClose(true)
        super.setParentBackground(Color.TRANSPARENT)
        super.setTransitionType(DialogTransition.CENTER)
    }

    companion object {

        fun builder(container: StackPane): Builder {
            return Builder(ChooseDialog(container))
        }
    }

    override fun initializeLayout(): BorderPane? {
        return Stream
            .of(DInterface.CHOOSE_DIALOG)
            .map(FXMLUtil::getFXMLLoader)
            .peek { it?.setController(this@ChooseDialog) }
            .map(FXMLUtil::loadInterface)
            .findFirst()
            .map(BorderPane::class.java::cast)
            .orElse(null)
    }

    override fun onDialogKeyPressed(event: KeyEvent) {
        //No need to implement
    }

    @FXML
    private fun onRadioButtonClicked(event: MouseEvent) {
        Optional.ofNullable(eventHandler).ifPresent { it.handle(event) }.also { close() }
    }

    /*=================================================={Builder}=====================================================*/
    class Builder(private val dialog: ChooseDialog) {

        fun setOnRadioButtonClicked(eventHandler: EventHandler<MouseEvent>): Builder {
            dialog.eventHandler = eventHandler
            return this
        }

        fun build(): ChooseDialog = dialog
    }
}