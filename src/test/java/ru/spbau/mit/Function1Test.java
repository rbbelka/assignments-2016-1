package ru.spbau.mit;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class Function1Test {

    private static final Function1<Integer, Double> SQRT = new Function1<Integer, Double>() {
        public Double apply(Integer arg) {
            return Math.sqrt(arg);
        }
    };

    private static final Function1<Integer, Integer> SQUARE = new Function1<Integer, Integer>() {
        public Integer apply(Integer arg) {
            return arg * arg;
        }
    };

    private static final Function1<Integer, Integer> INC = new Function1<Integer, Integer>() {
        public Integer apply(Integer arg) {
            return ++arg;
        }
    };

    private static final Function1<Integer, Integer> DEC = new Function1<Integer, Integer>() {
        public Integer apply(Integer arg) {
            return --arg;
        }
    };

    private static final Function1<String, String> SUFFIX = new Function1<String, String>() {
        public String apply(String arg1) {
            return arg1 + 's';
        }
    };


    @Test
    public void testApply() {
        assertEquals(9, SQUARE.apply(3), 1e-9);
        assertEquals(1, (int) INC.apply(0));
        assertEquals("tests", SUFFIX.apply("test"));
    }

    @Test
    public void testCompose() {

        assertEquals(12, SQUARE.compose(SQRT).apply(-12), 1e-9);
        assertEquals(6, (int) DEC.compose(INC).apply(6));
        assertEquals(10, (int) SQUARE.compose(INC).apply(-3));
        assertEquals(4, (int) INC.compose(SQUARE).apply(-3));
        assertEquals("less", SUFFIX.compose(SUFFIX).apply("le"));
    }
}