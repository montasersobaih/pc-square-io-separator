package com.mj.pc.square.io.separator.kotlin.dialog

import com.mj.pc.square.io.separator.kotlin.constant.DInterface
import com.mj.pc.square.io.separator.kotlin.util.FXMLUtil
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import java.util.Optional
import java.util.stream.Stream

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 15-10-2022
 */

class LoadingDialog(container: StackPane) : BaseDialog<BorderPane>(container) {

    @FXML
    private lateinit var loadingPane: BorderPane

    @FXML
    private lateinit var loadingBody: Label

    init {
        super.setParentBackground(Color.TRANSPARENT)
        super.setTransitionType(DialogTransition.CENTER)
        super.setEventHandler(KeyEvent.ANY, null)
    }

    companion object {

        fun builder(container: StackPane): Builder = Builder(LoadingDialog(container))
    }

    override fun initializeLayout(): BorderPane? {
        return Stream
            .of(DInterface.LOADING_DIALOG)
            .map(FXMLUtil::getFXMLLoader)
            .peek { it?.setController(this@LoadingDialog) }
            .map(FXMLUtil::loadInterface)
            .findFirst()
            .map(BorderPane::class.java::cast)
            .orElse(null)
    }

    fun setBodyMessage(message: String) = Optional.ofNullable(message).ifPresent(loadingBody::setText)

    /*=================================================={Builder}=====================================================*/
    class Builder(private val dialog: LoadingDialog) {

        fun setMessage(message: String): Builder {
            Optional.ofNullable(message).ifPresent(dialog::setBodyMessage)
            return this
        }

        fun build(): LoadingDialog = dialog
    }
}