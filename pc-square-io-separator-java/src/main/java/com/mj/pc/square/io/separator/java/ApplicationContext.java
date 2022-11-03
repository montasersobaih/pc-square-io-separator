package com.mj.pc.square.io.separator.java;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 20-10-2022
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicationContext {

    private static Path baseDirectory = Paths.get(System.getProperty("user.home"));

    public static void setDirectory(Path directory) {
        ApplicationContext.baseDirectory = directory;
    }

    public static Path getDirectory() {
        return baseDirectory;
    }
}
