package com.mj.pc.square.io.separator.java.exception;

import java.util.concurrent.ExecutionException;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 26-10-2022
 */

public final class WriteFileException extends ExecutionException {

    private final String message;

    public WriteFileException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
