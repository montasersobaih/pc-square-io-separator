package com.mj.pc.square.io.separator.java.exception;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 01-11-2022
 */

public class EmptyFileException extends ExecutionException {

    @Override
    protected String getExceptionKey() {
        return "exception.file.empty";
    }
}
