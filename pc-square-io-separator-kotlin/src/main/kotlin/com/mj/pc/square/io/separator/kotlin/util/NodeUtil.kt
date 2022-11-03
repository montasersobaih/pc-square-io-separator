package com.mj.pc.square.io.separator.kotlin.util

import javafx.scene.Node
import javafx.scene.control.ScrollBar
import javafx.scene.control.ScrollPane
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.util.Objects
import java.util.Optional
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 21-10-2022
 */

@Suppress("UNCHECKED_CAST")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
object NodeUtil {

    fun <R : Node> lookup(startFrom: Node, selector: String): Optional<R> {
        return Optional.of(selector).map(startFrom::lookup) as Optional<R>
    }

    fun <R : Node> lookupAll(startFrom: Node, selector: String): Set<R> = startFrom.lookupAll(selector) as Set<R>

    fun setVerticalScrolling(node: Node, scrollBar: ScrollBar) {
        node.setOnScroll {
            val value = it.deltaY

            if (value < 0) {
                scrollBar.increment()
            } else if (value > 0) {
                scrollBar.decrement()
            }
        }
    }

    fun setHorizontalScrolling(node: Node, scrollBar: ScrollBar) {
        node.setOnScroll {
            val value = it.deltaX

            if (value < 0) {
                scrollBar.increment()
            } else if (value > 0) {
                scrollBar.decrement()
            }
        }
    }

    fun setVerticalScrolling(node: Node, scrollPane: ScrollPane) = setVerticalScrolling(node, scrollPane, 0.001)

    fun setVerticalScrolling(node: Node, scrollPane: ScrollPane, scrolling: Double) {
        node.setOnScroll {
            val value = scrollPane.vvalue

            if (it.deltaY < 0) {
                if (value < scrollPane.vmax) {
                    scrollPane.vvalue = value + scrolling
                }
            } else {
                if (value > scrollPane.vmin) {
                    scrollPane.vvalue = value - scrolling
                }
            }
        }
    }

    fun <R> extractUserData(node: Node): R? = Optional.of(node).map(Node::getUserData).orElse(null) as R

    fun <R> extractUserData(nodes: List<Node>): List<R> {
        return Optional
            .of(nodes)
            .map { it.parallelStream() }
            .orElse(Stream.empty())
            .map(Node::getUserData)
            .filter(Objects::nonNull)
            .collect(Collectors.toList()) as List<R>
    }
}