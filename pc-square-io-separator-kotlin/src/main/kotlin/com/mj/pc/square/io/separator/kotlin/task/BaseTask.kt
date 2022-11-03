package com.mj.pc.square.io.separator.kotlin.task

import javafx.concurrent.Task

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 28-10-2022
 */

abstract class BaseTask<V> : Task<V>() {

    @Throws(Exception::class)
    abstract override fun call(): V
}