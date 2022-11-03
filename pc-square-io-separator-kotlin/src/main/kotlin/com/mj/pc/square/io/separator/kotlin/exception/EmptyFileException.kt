package com.mj.pc.square.io.separator.kotlin.exception

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 01-11-2022
 */

class EmptyFileException : ExecutionException() {

    override val exceptionKey: String
        get() = "exception.file.empty"
}