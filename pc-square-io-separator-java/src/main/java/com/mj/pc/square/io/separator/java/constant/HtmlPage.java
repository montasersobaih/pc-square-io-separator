package com.mj.pc.square.io.separator.java.constant;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 28-10-2022
 */

public enum HtmlPage {

    CONFIRMATION_PAGE(ResourcePath.STATIC.concat("confirmation.html"));

    private final String value;

    HtmlPage(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
