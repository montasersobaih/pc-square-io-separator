package com.mj.pc.square.io.separator.java.dialog;

import com.mj.pc.square.io.separator.java.constant.DInterface;
import com.mj.pc.square.io.separator.java.util.FXMLUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 15-10-2022
 */

public final class LoadingDialog extends BaseDialog<BorderPane> {

    @FXML
    private BorderPane loadingPane;

    @FXML
    private Label loadingBody;

    public LoadingDialog(StackPane container) {
        super(container);
        super.setParentBackground(Color.TRANSPARENT);
        super.setTransitionType(DialogTransition.CENTER);
        super.setEventHandler(KeyEvent.ANY, null);
    }

    public static Builder builder(StackPane container) {
        return new Builder(new LoadingDialog(container));
    }

    @Override
    protected BorderPane initializeLayout() {
        return Stream.of(DInterface.LOADING_DIALOG)
                .map(FXMLUtil::getFXMLLoader)
                .peek(loader -> loader.setController(LoadingDialog.this))
                .map(FXMLUtil::loadInterface)
                .findFirst()
                .map(BorderPane.class::cast)
                .orElse(null);
    }

    public void setBodyMessage(String message) {
        Optional.ofNullable(message).ifPresent(loadingBody::setText);
    }

    /*=================================================={Builder}=====================================================*/
    public static class Builder {

        private final LoadingDialog dialog;

        private Builder(LoadingDialog dialog) {
            this.dialog = dialog;
        }

        public Builder setMessage(String message) {
            Optional.ofNullable(message).ifPresent(dialog::setBodyMessage);
            return this;
        }

        public LoadingDialog build() {
            return dialog;
        }
    }
}