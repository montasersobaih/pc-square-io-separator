package com.mj.pc.square.io.separator.kotlin.constant

import lombok.AccessLevel
import lombok.NoArgsConstructor

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 15-10-2022
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
internal object ResourcePath {

    //==================================================={Interfaces}===================================================
    const val INTERFACE = "/interface/"

    const val DIALOG = INTERFACE + "dialog/"

    const val LAYOUT = INTERFACE + "layout/"

    //====================================================={Assist}=====================================================
    private const val ASSIST = "/assist/"

    const val STYLE_SHEET = ASSIST + "css/"

    const val STYLE_CONTROL = STYLE_SHEET + "control/"

    const val STYLE_USER_INTERFACE = STYLE_SHEET + "interface/"

    const val STYLE_DIALOG_INTERFACE = STYLE_USER_INTERFACE + "dialog/"

    const val STYLE_LAYOUT_INTERFACE = STYLE_USER_INTERFACE + "layout/"

    const val ICON = ASSIST + "icon/"

    const val IMAGE = ASSIST + "image/"

    //====================================================={Static}=====================================================
    const val STATIC = "/static/"
}