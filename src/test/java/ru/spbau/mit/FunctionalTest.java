package ru.spbau.mit;

import org.junit.Test;
import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class FunctionalTest {

    private static final List<Integer> TEST_LIST = asList(12, 6, -4, 27, 16, -12, 30);

    private static final List<Integer> TEST_NUMBERS = asList(10, 16, 24, 6);

    private static final List<String> S = asList("b", "c", "d");

    private static final List<Integer> TEST_EMPTY = new ArrayList<>();

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
            throw new RuntimeException();
        }
    };

    @Test
    public void testFunction1Apply() {
        assertEquals(9, SQUARE.apply(3), 1e-9);
        assertEquals(1, (int) INC.apply(0));
        assertEquals("tests", SUFFIX.apply("test"));
    }

    @Test
    public void testFunction1Compose() {

        for (int item : TEST_LIST) {
            assertEquals((double) Math.abs(item), SQUARE.compose(SQRT).apply(item), 1e-9);
            assertEquals(item, (int) INC.compose(DEC).apply(item));
            assertEquals(item, (int) DEC.compose(INC).apply(item));
        }
        assertEquals("less", SUFFIX.compose(SUFFIX).apply("le"));
    }

    @Test
    public void testFunction2Apply() {
        assertEquals(9, (int) SUM.apply(3, 6));
        assertEquals(0, (int) MULT.apply(0, 12));
        assertEquals("", CONCAT.apply("", ""));
        assertEquals("onetwo", CONCAT.apply("one", "two"));
    }

    @Test
    public void testFunction2Compose() {

        for (int item : TEST_LIST) {
            assertEquals(item + 3, (int) SUM.compose(INC).apply(item, 2));
            assertEquals(4 * item - 1, (int) MULT.compose(DEC).apply(item, 4));
        }
        assertEquals("onetwos", CONCAT.compose(SUFFIX).apply("one", "two"));
    }

    @Test
    public void testFunction2Bind1() {

        for (int item : TEST_LIST) {
            assertEquals(item + 3, (int) SUM.bind1(3).apply(item));
            assertEquals(4 * item, (int) MULT.bind1(4).apply(item));
        }
        assertEquals("onetwo", CONCAT.bind1("one").apply("two"));
    }

    @Test
    public void testFunction2Bind2() {

        for (int item : TEST_LIST) {
            assertEquals(item + 3, (int) SUM.bind2(3).apply(item));
            assertEquals(4 * item, (int) MULT.bind2(4).apply(item));
        }
        assertEquals("onetwo", CONCAT.bind2("two").apply("one"));
    }

    @Test
    public void testFunction2Curry() {

        Function1<Integer, Function1<Integer, Integer>> curMult = MULT.curry();
        Function1<String, Function1<String, String>> curConcat = CONCAT.curry();
        for (int item : TEST_LIST) {
            assertEquals(item * 3, (int) curMult.apply(item).apply(3));
            assertEquals("onetwo", curConcat.apply("one").apply("two"));
        }
    }

    @Test
    public void testPredicateAlways() {

        for (int item : TEST_LIST) {
            assertTrue(Predicate.ALWAYS_TRUE.apply(item));
            assertFalse(Predicate.ALWAYS_FALSE.apply(item));
        }
        assertTrue(Predicate.ALWAYS_TRUE.apply(false));
        assertFalse(Predicate.ALWAYS_FALSE.apply(true));
    }

    @Test
    public void testPredicateOr() {

        for (int item : TEST_LIST) {
            assertEquals(Math.abs(item) > 25, GREATER_25.or(LESS_NEG_25).apply(item));
            assertTrue(IS_POSITIVE.or(LESS_16).apply(item));
        }
        assertFalse(LESS_NEG_25.or(IS_POSITIVE).apply(-6));
        assertTrue(LESS_16.or(IS_REACHED).apply(8));
    }

    @Test
    public void testPredicateAnd() {

        for (int item : TEST_LIST) {
            assertEquals(Math.abs(item) < 16, GREATER_NEG_16.and(LESS_16).apply(item));
            assertFalse(IS_POSITIVE.and(LESS_NEG_25).apply(item));
        }
        assertFalse(LESS_16.and(IS_POSITIVE).apply(24));
        assertFalse(LESS_NEG_25.and(IS_REACHED).apply(-1));
    }

    @Test
    public void testPredicateNot() {

        for (int item : TEST_LIST) {
            assertEquals(Math.abs(item) >= 16, GREATER_NEG_16.and(LESS_16).not().apply(item));
            assertTrue(IS_POSITIVE.and(LESS_NEG_25).not().apply(item));
        }
        assertFalse(IS_POSITIVE.not().apply(24));
        assertTrue(GREATER_25.not().apply(8));
    }

    @Test
    public void testCollectionsMap() {
        Iterable<Integer> mapIter = Collections.map(SQUARE, TEST_LIST);
        Iterator<Integer> iterator = mapIter.iterator();
        assertNotNull(iterator);

        for (int item : TEST_LIST) {
            assertTrue(iterator.hasNext());
            assertEquals(item * item, (int) iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCollectionsFilter() {

        Iterable<Integer> filterIter = Collections.filter(IS_POSITIVE, TEST_LIST);
        Iterator<Integer> iterator = filterIter.iterator();
        assertNotNull(iterator);

        for (int item : TEST_LIST) {
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

        Iterable<Integer> takeWhileIter = Collections.takeWhile(LESS_16, TEST_LIST);
        Iterator<Integer> iterator = takeWhileIter.iterator();
        assertNotNull(iterator);

        for (int item : TEST_LIST) {
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

        Iterable<Integer> takeWhileIter = Collections.takeWhile(LESS_16, TEST_EMPTY);
        Iterator<Integer> iterator = takeWhileIter.iterator();
        assertNotNull(iterator);

        for (int item : TEST_EMPTY) {
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

        Iterable<Integer> takeUnlessIter = Collections.takeUnless(GREATER_25, TEST_LIST);
        Iterator<Integer> iterator = takeUnlessIter.iterator();
        assertNotNull(iterator);

        for (int item : TEST_LIST) {
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
        assertEquals(58, (int) Collections.foldl(SUM, 2, TEST_NUMBERS));
        assertEquals("abcd", Collections.foldl(CONCAT, "a", S));
    }

    @Test
    public void testCollectionsFoldr() {
        assertEquals(5.0, Collections.foldr(DIVIDE, 2.0, TEST_NUMBERS), 1e-9);
        assertEquals("bcda", Collections.foldr(CONCAT, "a", S));
    }

}
