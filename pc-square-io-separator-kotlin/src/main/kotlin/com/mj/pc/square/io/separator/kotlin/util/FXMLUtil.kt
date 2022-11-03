package com.mj.pc.square.io.separator.kotlin.util

import javafx.fxml.FXMLLoader
import javafx.scene.layout.Pane
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.io.IOException
import java.util.Optional
import java.util.ResourceBundle
import java.util.stream.Stream

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @since 31-07-2021
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
object FXMLUtil {

    fun getFXMLLoader(fxInterface: FXInterface): FXMLLoader? {
        return Stream.of(fxInterface)
            .map(FXInterface::toString)
            .map(FXMLUtil::class.java::getResource)
            .map(::FXMLLoader)
            .peek {
                Optional
                    .of("controls")
                    .map(ResourceBundle::getBundle)
                    .ifPresent(it!!::setResources)
            }
            .findFirst()
            .orElse(null)
    }

    fun loadInterface(loader: FXMLLoader?): Pane? {
        var pane: Pane? = null

        try {
            pane = loader!!.load()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return pane
    }

    fun loadInterface(fxInterface: FXInterface): Pane? {
        return Optional.of(fxInterface)
            .map(::getFXMLLoader)
            .map(::loadInterface)
            .orElse(null)
    }
}