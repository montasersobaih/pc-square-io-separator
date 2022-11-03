package com.mj.pc.square.io.separator.kotlin.exception

import lombok.NoArgsConstructor
import java.util.Optional
import java.util.ResourceBundle
import java.util.concurrent.ExecutionException

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 28-10-2022
 */

@NoArgsConstructor
abstract class ExecutionException : ExecutionException {

    companion object {

        private val resources = ResourceBundle.getBundle("exceptions")
    }

    constructor() : super()

    constructor(message: String?) : super(message)

    protected abstract val exceptionKey: String?

    override val message: String?
        get() = Optional.ofNullable(exceptionKey).map(resources::getString).orElse(null)
}