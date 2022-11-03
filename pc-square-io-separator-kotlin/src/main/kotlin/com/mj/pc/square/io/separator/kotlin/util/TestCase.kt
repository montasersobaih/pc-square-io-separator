package com.mj.pc.square.io.separator.kotlin.util

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 20-10-2022
 */

data class TestCase(val input: String, val output: String) {

    override fun toString(): String = "$input\n-\n$output"

    companion object {

        fun parse(testCase: String): TestCase {
            val io = testCase.split("\n*\\-+\n*".toRegex())
            return TestCase(io[0], io[1])
        }
    }
}