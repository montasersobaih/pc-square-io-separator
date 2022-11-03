package com.mj.pc.square.io.separator.java.controller.layout;

import com.mj.pc.square.io.separator.java.controller.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 21-10-2022
 */

public final class DataLayoutController extends BaseController {

    @FXML
    private BorderPane dataPane;

    @FXML
    private Label title;

    @FXML
    private Label body;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setTitle(String title) {
        Optional.ofNullable(title).ifPresent(this.title::setText);
    }

    public void setBody(String body) {
        Optional.ofNullable(body).ifPresent(this.body::setText);
    }
}
