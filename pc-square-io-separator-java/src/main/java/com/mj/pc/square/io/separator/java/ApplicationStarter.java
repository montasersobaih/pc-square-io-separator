package com.mj.pc.square.io.separator.java;

import com.mj.pc.square.io.separator.java.constant.UInterface;
import com.mj.pc.square.io.separator.java.util.FXMLUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 15-10-2022
 */

public final class ApplicationStarter extends Application {

    @Override
    public void start(Stage stage) {
        Pane pane = FXMLUtil.loadInterface(UInterface.APPLICATION_PAGE);

        stage.setTitle("PC^2 Test Case Separator");
        stage.setScene(new Scene(pane, 800, 500));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}