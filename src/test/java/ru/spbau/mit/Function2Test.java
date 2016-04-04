package ru.spbau.mit;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class Function2Test {

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

    private static final Function2<Integer, Integer, Integer> SUM = new Function2<Integer, Integer, Integer>() {
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 + arg2;
        }
    };

    private static final Function2<Integer, Integer, Integer> MULT = new Function2<Integer, Integer, Integer>() {
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 * arg2;
        }
    };

    private static final Function2<String, String, String> CONCAT = new Function2<String, String, String>() {
        public String apply(String arg1, String arg2) {
            return arg1 + arg2;
        }
    };


    @Test
    public void testApply() {
        assertEquals(9, (int) SUM.apply(3, 6));
        assertEquals(0, (int) MULT.apply(0, 12));
        assertEquals("", CONCAT.apply("", ""));
        assertEquals("onetwo", CONCAT.apply("one", "two"));
    }

    @Test
    public void testCompose() {
        assertEquals(-1, (int) SUM.compose(INC).apply(-4, 2));
        assertEquals(107, (int) MULT.compose(DEC).apply(27, 4));
        assertEquals(9, (int) SUM.compose(DEC).apply(3, 7));
        assertEquals(41, (int) MULT.compose(INC).apply(10, 4));
        assertEquals("onetwos", CONCAT.compose(SUFFIX).apply("one", "two"));
    }

    @Test
    public void testBind1() {
        assertEquals(19, (int) SUM.bind1(3).apply(16));
        assertEquals(48, (int) MULT.bind1(4).apply(12));
        assertEquals("onetwo", CONCAT.bind1("one").apply("two"));
    }

    @Test
    public void testBind2() {
        assertEquals(33, (int) SUM.bind2(3).apply(30));
        assertEquals(20, (int) MULT.bind2(4).apply(5));
        assertEquals("onetwo", CONCAT.bind2("two").apply("one"));
    }

    @Test
    public void testCurry() {
        assertEquals(18, (int) MULT.curry().apply(6).apply(3));
        assertEquals("onetwo",  CONCAT.curry().apply("one").apply("two"));
    }

}