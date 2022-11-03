package com.mj.pc.square.io.separator.java.util;

import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 26-10-2022
 */

@NoArgsConstructor
public final class SyncValue<T> {

    private T value;

    public synchronized void setValue(T value) {
        this.value = value;
        this.notify();
    }

    public synchronized T getValue() throws InterruptedException {
        if (Objects.isNull(value)) {
            this.wait();
        }

        return value;
    }
}
