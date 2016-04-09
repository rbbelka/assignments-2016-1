package ru.spbau.mit;

import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public class CollectionsTest {

    private static final Function1<Integer, Integer> SQUARE = new Function1<Integer, Integer>() {
        public Integer apply(Integer arg) {
            return arg * arg;
        }
    };

    private static final Function2<Integer, Integer, Integer> SUM = new Function2<Integer, Integer, Integer>() {
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 + arg2;
        }
    };

    private static final Function2<String, String, String> CONCAT = new Function2<String, String, String>() {
        public String apply(String arg1, String arg2) {
            return arg1 + arg2;
        }
    };

    private static final Function2<Integer, Double, Double> DIVIDE = new Function2<Integer, Double, Double>() {
        public Double apply(Integer firstArgument, Double secondArgument) {
            return firstArgument / secondArgument;
        }
    };

    private static final Predicate<Integer> GREATER_25 = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            return arg > 25;
        }
    };

    private static final Predicate<Integer> LESS_16 = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            return arg < 16;
        }
    };

    private static final Predicate<Integer> IS_POSITIVE = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            return arg > 0;
        }
    };

    @Test
    public void testCollectionsMap() {
        List<Integer> list = asList(12, 6, -4, 27, 16, -12, 30);
        assertEquals(asList(144, 36, 16, 729, 256, 144, 900), Collections.map(SQUARE, list));
    }

    @Test
    public void testCollectionsFilter() {
        List<Integer> list = asList(12, 6, -4, 27, 16, -12, 30);
        assertEquals(asList(12, 6, 27, 16, 30), Collections.filter(IS_POSITIVE, list));
    }

    @Test
    public void testCollectionsTakeWhile() {
        List<Integer> list = asList(12, 6, -4, 27, 16, -12, 30);
        assertEquals(asList(12, 6, -4), Collections.takeWhile(LESS_16, list));
    }

    @Test
    public void testCollectionsTakeWhileEmpty() {
        final List<Integer> emptyList = emptyList();
        assertEquals(asList(), Collections.takeWhile(LESS_16, emptyList));
    }

    @Test
    public void testCollectionsTakeUnless() {
        List<Integer> list = asList(12, 6, -4, 27, 16, -12, 30);
        assertEquals(asList(12, 6, -4), Collections.takeUnless(GREATER_25, list));
    }

    @Test
    public void testCollectionsFoldl() {
        List<Integer> list = asList(10, 16, 24, 6);
        assertEquals(58, (int) Collections.foldl(SUM, 2, list));
        assertEquals("abcd", Collections.foldl(CONCAT, "a", asList("b", "c", "d")));
    }

    @Test
    public void testCollectionsFoldr() {
        List<Integer> list = asList(10, 16, 24, 6);
        assertEquals(5.0, Collections.foldr(DIVIDE, 2.0, list), 1e-9);
        assertEquals("bcda", Collections.foldr(CONCAT, "a", asList("b", "c", "d")));
    }

}
