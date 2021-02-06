package io.github.syske.common.util;

import org.junit.Test;

public class CommonUtilTest {
    @Test
    public void testIsNull() {
        String sValue = "   ";
        boolean equals = sValue.trim().equals("");
        System.out.println(equals);
    }
}
