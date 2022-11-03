package com.mj.pc.square.io.separator.kotlin.controller.layout

import com.mj.pc.square.io.separator.kotlin.controller.BaseController
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import java.net.URL
import java.util.Optional
import java.util.ResourceBundle

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 21-10-2022
 */

class DataLayoutController : BaseController() {

    @FXML
    private lateinit var dataPane: BorderPane

    @FXML
    private lateinit var title: Label

    @FXML
    private lateinit var body: Label

    override fun initialize(location: URL, resources: ResourceBundle) {}

    fun setTitle(title: String?) {
        Optional.ofNullable(title).ifPresent { this.title.text = it }
    }

    fun setBody(body: String?) {
        Optional.ofNullable(body).ifPresent { this.body.text = it }
    }
}