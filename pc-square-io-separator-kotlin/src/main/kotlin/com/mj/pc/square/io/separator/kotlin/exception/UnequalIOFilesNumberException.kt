package com.mj.pc.square.io.separator.kotlin.exception

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 29-10-2022
 */

class UnequalIOFilesNumberException : ExecutionException() {

    override val exceptionKey: String
        get() = "exception.directory.files.io.unequal"
}