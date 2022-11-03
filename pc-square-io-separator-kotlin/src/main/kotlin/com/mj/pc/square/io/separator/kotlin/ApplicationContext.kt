package com.mj.pc.square.io.separator.kotlin

import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 20-10-2022
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
object ApplicationContext {

    var directory: Path = Paths.get(System.getProperty("user.home"))
}