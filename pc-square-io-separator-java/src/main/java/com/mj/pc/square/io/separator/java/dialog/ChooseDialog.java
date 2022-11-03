package com.mj.pc.square.io.separator.java.dialog;

import com.mj.pc.square.io.separator.java.constant.DInterface;
import com.mj.pc.square.io.separator.java.util.FXMLUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Montaser Sobaih
 * @version 1.0
 * @since 13-06-2021
 */

public final class ChooseDialog extends BaseDialog<BorderPane> {

    @FXML
    private BorderPane choosePane;

    private EventHandler<MouseEvent> eventHandler;

    private ChooseDialog(StackPane container) {
        super(container);
        super.setOverlayClose(true);
        super.setParentBackground(Color.TRANSPARENT);
        super.setTransitionType(DialogTransition.CENTER);
    }

    public static Builder builder(StackPane container) {
        return new Builder(new ChooseDialog(container));
    }

    @Override
    protected BorderPane initializeLayout() {
        return Stream.of(DInterface.CHOOSE_DIALOG)
                .map(FXMLUtil::getFXMLLoader)
                .peek(loader -> loader.setController(ChooseDialog.this))
                .map(FXMLUtil::loadInterface)
                .findFirst()
                .map(BorderPane.class::cast)
                .orElse(null);
    }

    @Override
    protected void onDialogKeyPressed(KeyEvent event) {
        //No need to implement
    }

    @FXML
    private void onRadioButtonClicked(MouseEvent event) {
        Optional.ofNullable(eventHandler).ifPresent(e -> e.handle(event));
        this.close();
    }

    /*=================================================={Builder}=====================================================*/
    public static class Builder {

        private final ChooseDialog dialog;

        private Builder(ChooseDialog dialog) {
            this.dialog = dialog;
        }

        public Builder setOnRadioButtonClicked(EventHandler<MouseEvent> eventHandler) {
            dialog.eventHandler = eventHandler;
            return this;
        }

        public ChooseDialog build() {
            return dialog;
        }
    }
}