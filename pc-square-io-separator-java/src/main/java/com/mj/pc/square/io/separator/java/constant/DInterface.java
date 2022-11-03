package com.mj.pc.square.io.separator.java.constant;

import com.mj.pc.square.io.separator.java.util.FXInterface;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 15-10-2022
 */

public enum DInterface implements FXInterface {

    ALERT_DIALOG(ResourcePath.DIALOG.concat("alert_dialog.fxml")),
    CHOOSE_DIALOG(ResourcePath.DIALOG.concat("choose_dialog.fxml")),
    LOADING_DIALOG(ResourcePath.DIALOG.concat("loading_dialog.fxml")),
    CONFIRM_DIALOG(ResourcePath.DIALOG.concat("confirm_dialog.fxml"));

    private final String value;

    DInterface(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
