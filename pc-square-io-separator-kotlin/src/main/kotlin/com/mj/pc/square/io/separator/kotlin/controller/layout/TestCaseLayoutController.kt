package com.mj.pc.square.io.separator.kotlin.controller.layout

import com.mj.pc.square.io.separator.kotlin.controller.BaseController
import com.mj.pc.square.io.separator.kotlin.util.TestCase
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import java.net.URL
import java.util.Objects
import java.util.Optional
import java.util.ResourceBundle

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 21-10-2022
 */

class TestCaseLayoutController : BaseController() {

    @FXML
    private lateinit var testCasePane: BorderPane

    @FXML
    private lateinit var title: Label

    @FXML
    private lateinit var inputController: DataLayoutController

    @FXML
    private lateinit var outputController: DataLayoutController

    override fun initialize(location: URL, resources: ResourceBundle) {
        inputController.setTitle(getString("control.label.input"))
        outputController.setTitle(getString("control.label.output"))
    }

    fun setTitle(title: String?) {
        Optional.ofNullable(title).ifPresent { this.title.text = it }
    }

    fun setTestCase(testCase: TestCase) {
        if (Objects.nonNull(testCase)) {
            inputController.setBody(testCase.input)
            outputController.setBody(testCase.output)
        }
    }
}