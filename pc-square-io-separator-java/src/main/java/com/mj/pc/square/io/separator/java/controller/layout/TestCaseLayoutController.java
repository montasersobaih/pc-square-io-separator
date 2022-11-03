package com.mj.pc.square.io.separator.java.controller.layout;

import com.mj.pc.square.io.separator.java.controller.BaseController;
import com.mj.pc.square.io.separator.java.util.TestCase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 21-10-2022
 */

public final class TestCaseLayoutController extends BaseController {

    @FXML
    private BorderPane testCasePane;

    @FXML
    private Label title;

    @FXML
    private DataLayoutController inputController;

    @FXML
    private DataLayoutController outputController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputController.setTitle(getString("control.label.input"));
        outputController.setTitle(getString("control.label.output"));
    }

    public void setTitle(String title) {
        Optional.ofNullable(title).ifPresent(this.title::setText);
    }

    public void setTestCase(TestCase testCase) {
        if (Objects.nonNull(testCase)) {
            inputController.setBody(testCase.getInput());
            outputController.setBody(testCase.getOutput());
        }
    }
}
