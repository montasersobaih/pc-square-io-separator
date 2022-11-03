package com.mj.pc.square.io.separator.kotlin.constant

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 15-10-2022
 */

enum class StyleSheet(private val value: String) {

    GLOBAL_COLORS("${ResourcePath.STYLE_SHEET}global-colors.css"),
    APP_LAYOUT("${ResourcePath.STYLE_SHEET}app-layout.css"),
    BUTTON("${ResourcePath.STYLE_CONTROL}button.css"),
    DIALOG("${ResourcePath.STYLE_CONTROL}dialog.css"),
    LABEL("${ResourcePath.STYLE_CONTROL}label.css"),
    PADDED_DIALOG("${ResourcePath.STYLE_CONTROL}padded-dialog.css"),
    PADDED_PANE("${ResourcePath.STYLE_CONTROL}padded-pane.css"),
    PANE("${ResourcePath.STYLE_CONTROL}pane.css"),
    PROGRESS_INDICATOR("${ResourcePath.STYLE_CONTROL}progress-indicator.css"),
    SCROLL_BAR("${ResourcePath.STYLE_CONTROL}scroll-bar.css"),
    SCROLL_PANE("${ResourcePath.STYLE_CONTROL}scroll-pane.css"),
    TEXT_FIELD("${ResourcePath.STYLE_CONTROL}text-field.css");

    override fun toString(): String = value
}