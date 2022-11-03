package com.mj.pc.square.io.separator.java.exception;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 29-10-2022
 */

public final class LoadInputFileException extends ExecutionException {

    @Override
    protected String getExceptionKey() {
        return "exception.file.input.load";
    }
}
