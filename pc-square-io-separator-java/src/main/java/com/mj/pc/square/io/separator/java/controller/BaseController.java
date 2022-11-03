package com.mj.pc.square.io.separator.java.controller;

import com.mj.pc.square.io.separator.java.util.ResourceBundleUtil;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 20-10-2022
 */

public abstract class BaseController implements Initializable {

    protected static final long SCHEDULE_DELAY = 100;

    protected static final TimeUnit DELAY_TIMEUNIT = TimeUnit.MILLISECONDS;

    protected final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override
    public abstract void initialize(URL location, ResourceBundle resources);

    protected final String getString(String key) {
        return ResourceBundleUtil.getString(key);
    }
}
