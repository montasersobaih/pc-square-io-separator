package com.mj.pc.square.io.separator.java.exception;

import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 28-10-2022
 */

@NoArgsConstructor
public abstract class ExecutionException extends java.util.concurrent.ExecutionException {

    private static final ResourceBundle resources = ResourceBundle.getBundle("exceptions");

    public ExecutionException(String message) {
        super(message);
    }

    protected abstract String getExceptionKey();

    @Override
    public final String getMessage() {
        return Optional
                .ofNullable(getExceptionKey())
                .map(resources::getString)
                .orElse(null);
    }
}
