package com.mj.pc.square.io.separator.java.constant;

import com.mj.pc.square.io.separator.java.util.FXInterface;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 15-10-2022
 */

public enum UInterface implements FXInterface {

    APPLICATION_PAGE(ResourcePath.INTERFACE.concat("view.fxml"));

    private final String value;

    UInterface(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
