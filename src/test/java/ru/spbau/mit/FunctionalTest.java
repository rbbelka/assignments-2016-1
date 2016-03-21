package ru.spbau.mit;

import org.junit.Test;
import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class FunctionalTest {

    private List<Integer> testList = asList(12, 6, -4, 27, 16, -12, 30);

    private List<Integer> testNumbers = asList(10, 16, 24, 6);

    private List<String> s = asList("b", "c", "d");

    private List<Integer> testEmpty = new ArrayList<>();

    private Function1<Integer, Double> sqrt = new Function1<Integer, Double>() {
        public Double apply(Integer arg) {
            return Math.sqrt(arg);
        }
    };

    private Function1<Integer, Integer> square = new Function1<Integer, Integer>() {
        public Integer apply(Integer arg) {
            return arg * arg;
        }
    };

    private Function1<Integer, Integer> inc = new Function1<Integer, Integer>() {
        public Integer apply(Integer arg) {
            return ++arg;
        }
    };

    private Function1<Integer, Integer> dec = new Function1<Integer, Integer>() {
        public Integer apply(Integer arg) {
            return --arg;
        }
    };

    private Function1<String, String> suffix = new Function1<String, String>() {
        public String apply(String arg1) {
            return arg1 + 's';
        }
    };

    private Function2<Integer, Integer, Integer> sum = new Function2<Integer, Integer, Integer>() {
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 + arg2;
        }
    };

    private Function2<Integer, Integer, Integer> mult = new Function2<Integer, Integer, Integer>() {
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 * arg2;
        }
    };

    private Function2<String, String, String> concat = new Function2<String, String, String>() {
        public String apply(String arg1, String arg2) {
            return arg1 + arg2;
        }
    };

    private Function2<Integer, Double, Double> divide = new Function2<Integer, Double, Double>() {
        public Double apply(Integer firstArgument, Double secondArgument) {
            return firstArgument / secondArgument;
        }
    };

    private Predicate<Integer> greater25 = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            return arg > 25;
        }
    };

    private Predicate<Integer> lessNeg25 = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            return arg < -25;
        }
    };

    private Predicate<Integer> less16 = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            return arg < 16;
        }
    };

    private Predicate<Integer> greaterNeg16 = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            return arg > -16;
        }
    };

    private Predicate<Integer> isPositive = new Predicate<Integer>() {
        public Boolean apply(Integer arg) {
            return arg > 0;
        }
    };

    @Test
    public void testFunction1Apply() {
        assertEquals(9, square.apply(3), 1e-9);
        assertEquals(1, (int) inc.apply(0));
        assertEquals("tests", suffix.apply("test"));
    }

    @Test
    public void testFunction1Compose() {

        for (int item : testList) {
            assertEquals((double) Math.abs(item), square.compose(sqrt).apply(item), 1e-9);
            assertEquals(item, (int) inc.compose(dec).apply(item));
            assertEquals(item, (int) dec.compose(inc).apply(item));
        }
        assertEquals("less", suffix.compose(suffix).apply("le"));
    }

    @Test
    public void testFunction2Apply() {
        assertEquals(9, (int) sum.apply(3, 6));
        assertEquals(0, (int) mult.apply(0, 12));
        assertEquals("", concat.apply("", ""));
        assertEquals("onetwo", concat.apply("one", "two"));
    }

    @Test
    public void testFunction2Compose() {

        for (int item : testList) {
            assertEquals(item + 3, (int) sum.compose(inc).apply(item, 2));
            assertEquals(4 * item - 1, (int) mult.compose(dec).apply(item, 4));
        }
        assertEquals("onetwos", concat.compose(suffix).apply("one", "two"));
    }

    @Test
    public void testFunction2Bind1() {

        for (int item : testList) {
            assertEquals(item + 3, (int) sum.bind1(3).apply(item));
            assertEquals(4 * item, (int) mult.bind1(4).apply(item));
        }
        assertEquals("onetwo", concat.bind1("one").apply("two"));
    }

    @Test
    public void testFunction2Bind2() {

        for (int item : testList) {
            assertEquals(item + 3, (int) sum.bind2(3).apply(item));
            assertEquals(4 * item, (int) mult.bind2(4).apply(item));
        }
        assertEquals("onetwo", concat.bind2("two").apply("one"));
    }

    @Test
    public void testFunction2Curry() {

        Function1<Integer, Function1<Integer, Integer>> curMult = mult.curry();
        Function1<String, Function1<String, String>> curConcat = concat.curry();
        for (int item : testList) {
            assertEquals(item * 3, (int) curMult.apply(item).apply(3));
            assertEquals("onetwo", curConcat.apply("one").apply("two"));
        }
    }

    @Test
    public void testPredicateAlways() {

        for (int item : testList) {
            assertTrue(Predicate.ALWAYS_TRUE.apply(item));
            assertFalse(Predicate.ALWAYS_FALSE.apply(item));
        }
        assertTrue(Predicate.ALWAYS_TRUE.apply(false));
        assertFalse(Predicate.ALWAYS_FALSE.apply(true));
    }

    @Test
    public void testPredicateOr() {

        for (int item : testList) {
            assertEquals(Math.abs(item) > 25, greater25.or(lessNeg25).apply(item));
            assertTrue(isPositive.or(less16).apply(item));
        }
        assertFalse(lessNeg25.or(isPositive).apply(-6));
    }

    @Test
    public void testPredicateAnd() {

        for (int item : testList) {
            assertEquals(Math.abs(item) < 16, greaterNeg16.and(less16).apply(item));
            assertFalse(isPositive.and(lessNeg25).apply(item));
        }
        assertFalse(less16.and(isPositive).apply(24));
    }

    @Test
    public void testPredicateNot() {

        for (int item : testList) {
            assertEquals(Math.abs(item) >= 16, greaterNeg16.and(less16).not().apply(item));
            assertTrue(isPositive.and(lessNeg25).not().apply(item));
        }
        assertFalse(isPositive.not().apply(24));
        assertTrue(greater25.not().apply(8));
    }

    @Test
    public void testCollectionsMap() {
        Iterable<Integer> mapIter = Collections.map(square, testList);
        Iterator<Integer> iterator = mapIter.iterator();
        assertNotNull(iterator);

        for (int item : testList) {
            assertTrue(iterator.hasNext());
            assertEquals(item * item, (int) iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCollectionsFilter() {

        Iterable<Integer> filterIter = Collections.filter(isPositive, testList);
        Iterator<Integer> iterator = filterIter.iterator();
        assertNotNull(iterator);

        for (int item : testList) {
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

        Iterable<Integer> takeWhileIter = Collections.takeWhile(less16, testList);
        Iterator<Integer> iterator = takeWhileIter.iterator();
        assertNotNull(iterator);

        for (int item : testList) {
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

        Iterable<Integer> takeWhileIter = Collections.takeWhile(less16, testEmpty);
        Iterator<Integer> iterator = takeWhileIter.iterator();
        assertNotNull(iterator);

        for (int item : testEmpty) {
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

        Iterable<Integer> takeUnlessIter = Collections.takeUnless(greater25, testList);
        Iterator<Integer> iterator = takeUnlessIter.iterator();
        assertNotNull(iterator);

        for (int item : testList) {
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
        assertEquals(58, (int) Collections.foldl(sum, 2, testNumbers));
        assertEquals("abcd", Collections.foldl(concat, "a", s));
    }

    @Test
    public void testCollectionsFoldr() {
        assertEquals(5.0, Collections.foldr(divide, 2.0, testNumbers), 1e-9);
        assertEquals("bcda", Collections.foldr(concat, "a", s));
    }

}
