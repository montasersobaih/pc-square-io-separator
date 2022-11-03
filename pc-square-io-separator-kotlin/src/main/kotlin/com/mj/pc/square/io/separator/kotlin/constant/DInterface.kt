package com.mj.pc.square.io.separator.kotlin.constant

import com.mj.pc.square.io.separator.kotlin.util.FXInterface

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 15-10-2022
 */

enum class DInterface(private val value: String) : FXInterface {

    ALERT_DIALOG("${ResourcePath.DIALOG}alert_dialog.fxml"),
    CHOOSE_DIALOG("${ResourcePath.DIALOG}choose_dialog.fxml"),
    LOADING_DIALOG("${ResourcePath.DIALOG}loading_dialog.fxml"),
    CONFIRM_DIALOG("${ResourcePath.DIALOG}confirm_dialog.fxml");

    override fun toString(): String = value
}