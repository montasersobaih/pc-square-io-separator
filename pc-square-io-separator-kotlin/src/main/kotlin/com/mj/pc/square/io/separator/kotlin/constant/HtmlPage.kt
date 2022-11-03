package com.mj.pc.square.io.separator.kotlin.constant

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 28-10-2022
 */

enum class HtmlPage(private val value: String) {

    CONFIRMATION_PAGE("${ResourcePath.STATIC}confirmation.html");

    override fun toString(): String = value
}