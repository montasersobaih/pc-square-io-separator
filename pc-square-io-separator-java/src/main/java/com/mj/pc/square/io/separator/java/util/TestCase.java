package com.mj.pc.square.io.separator.java.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Montaser Sbaih
 * @version 1.0
 * @email montaser.jjs@gmail.com
 * @phone +962-786258874
 * @since 20-10-2022
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class TestCase {

    private String input;

    private String output;

    @Override
    public String toString() {
        return String.format("%s\n-\n%s", input, output);
    }

    public static TestCase parse(String testCase) {
        String[] IO = testCase.split("\n*\\-+\n*");
        return new TestCase(IO[0], IO[1]);
    }
}
