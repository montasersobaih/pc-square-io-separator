package com.mj.pc.square.io.separator.kotlin.constant

import com.mj.pc.square.io.separator.kotlin.util.FXInterface

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 15-10-2022
 */

enum class Layout(private val value: String) : FXInterface {

    DATA_LAYOUT("${ResourcePath.LAYOUT}data.fxml"),
    TEST_CASE_LAYOUT("${ResourcePath.LAYOUT}test_case.fxml");

    override fun toString(): String = value
}