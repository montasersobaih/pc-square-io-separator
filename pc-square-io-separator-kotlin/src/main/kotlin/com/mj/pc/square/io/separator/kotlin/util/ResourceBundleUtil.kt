package com.mj.pc.square.io.separator.kotlin.util

import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.util.Objects
import java.util.ResourceBundle

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 31-10-2022
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
object ResourceBundleUtil {

    private val resourcesBaseNames = arrayOf("controls", "exceptions", "messages")

    private val resources: MutableList<ResourceBundle> = ArrayList()

    init {
        for (baseName in resourcesBaseNames) {
            resources.add(ResourceBundle.getBundle(baseName))
        }
    }

    fun getString(key: String): String {
        if (Objects.nonNull(key)) {
            for (resource in resources) {
                if (resource.containsKey(key)) {
                    return resource.getString(key)
                }
            }
        }

        return key
    }
}