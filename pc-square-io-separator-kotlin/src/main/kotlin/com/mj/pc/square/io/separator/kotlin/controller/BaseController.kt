package com.mj.pc.square.io.separator.kotlin.controller

import com.mj.pc.square.io.separator.kotlin.util.ResourceBundleUtil
import javafx.fxml.Initializable
import java.net.URL
import java.util.ResourceBundle
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 20-10-2022
 */

abstract class BaseController : Initializable {

    abstract override fun initialize(location: URL, resources: ResourceBundle)

    protected fun getString(key: String): String = ResourceBundleUtil.getString(key)

    companion object {

        @JvmStatic
        protected val SCHEDULE_DELAY: Long = 100

        @JvmStatic
        protected val DELAY_TIMEUNIT = TimeUnit.MILLISECONDS

        @JvmStatic
        protected val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    }
}