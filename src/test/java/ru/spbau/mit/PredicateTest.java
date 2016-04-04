package ru.spbau.mit;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class PredicateTest {

    private static final Predicate<Integer> GREATER_25 = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            return arg > 25;
        }
    };

    private static final Predicate<Integer> LESS_NEG_25 = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            return arg < -25;
        }
    };

    private static final Predicate<Integer> LESS_16 = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            return arg < 16;
        }
    };

    private static final Predicate<Integer> GREATER_NEG_16 = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            return arg > -16;
        }
    };

    private static final Predicate<Integer> IS_POSITIVE = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            return arg > 0;
        }
    };

    private static final Predicate<Integer> IS_REACHED = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            fail("Lazyness does not work");
            return false;
        }
    };

    @Test
    public void testPredicateAlways() {
        assertTrue(Predicate.ALWAYS_TRUE.apply(true));
        assertTrue(Predicate.ALWAYS_TRUE.apply(false));
        assertFalse(Predicate.ALWAYS_FALSE.apply(true));
        assertFalse(Predicate.ALWAYS_FALSE.apply(false));
    }

    @Test
    public void testPredicateOr() {
        assertTrue(GREATER_25.or(LESS_NEG_25).apply(27));
        assertFalse(GREATER_25.or(LESS_NEG_25).apply(4));
        assertTrue(IS_POSITIVE.or(LESS_16).apply(-56));
        assertFalse(LESS_NEG_25.or(IS_POSITIVE).apply(-6));
        assertTrue(LESS_16.or(IS_REACHED).apply(8));
    }

    @Test
    public void testPredicateAnd() {
        assertTrue(GREATER_NEG_16.and(LESS_16).apply(3));
        assertFalse(GREATER_NEG_16.and(LESS_16).apply(21));
        assertFalse(IS_POSITIVE.and(LESS_NEG_25).apply(342));
        assertFalse(LESS_16.and(IS_POSITIVE).apply(24));
        assertFalse(LESS_NEG_25.and(IS_REACHED).apply(-1));
    }

    @Test
    public void testPredicateNot() {
        assertTrue(LESS_16.not().apply(16));
        assertFalse(GREATER_NEG_16.not().apply(0));
        assertTrue(LESS_NEG_25.not().apply(55));
        assertFalse(IS_POSITIVE.not().apply(24));
        assertTrue(GREATER_25.not().apply(8));
    }

}