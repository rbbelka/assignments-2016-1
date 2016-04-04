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
        Iterable<Integer> mapIter = Collections.map(SQUARE, list);
        Iterator<Integer> iterator = mapIter.iterator();
        assertNotNull(iterator);

        for (int item : list) {
            assertTrue(iterator.hasNext());
            assertEquals(item * item, (int) iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCollectionsFilter() {

        List<Integer> list = asList(12, 6, -4, 27, 16, -12, 30);
        Iterable<Integer> filterIter = Collections.filter(IS_POSITIVE, list);
        Iterator<Integer> iterator = filterIter.iterator();
        assertNotNull(iterator);

        for (int item : list) {
            if (item <= 0) {
                continue;
            }
            assertTrue(iterator.hasNext());
            assertEquals(item, (int) iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCollectionsTakeWhile() {

        List<Integer> list = asList(12, 6, -4, 27, 16, -12, 30);
        Iterable<Integer> takeWhileIter = Collections.takeWhile(LESS_16, list);
        Iterator<Integer> iterator = takeWhileIter.iterator();
        assertNotNull(iterator);

        for (int item : list) {
            if (item >= 16) {
                break;
            }
            assertTrue(iterator.hasNext());
            assertEquals(item, (int) iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCollectionsTakeWhileEmpty() {
        final List<Integer> EMPTY_LIST = emptyList();

        Iterable<Integer> takeWhileIter = Collections.takeWhile(LESS_16, EMPTY_LIST);
        Iterator<Integer> iterator = takeWhileIter.iterator();
        assertNotNull(iterator);

        for (int item : EMPTY_LIST) {
            if (item >= 16) {
                break;
            }
            assertTrue(iterator.hasNext());
            assertEquals(item, (int) iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCollectionsTakeUnless() {

        List<Integer> list = asList(12, 6, -4, 27, 16, -12, 30);
        Iterable<Integer> takeUnlessIter = Collections.takeUnless(GREATER_25, list);
        Iterator<Integer> iterator = takeUnlessIter.iterator();
        assertNotNull(iterator);

        for (int item : list) {
            if (item > 25) {
                break;
            }
            assertTrue(iterator.hasNext());
            assertEquals(item, (int) iterator.next());
        }
        assertFalse(iterator.hasNext());
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