package ru.spbau.mit;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class StringSetTest {

@org.junit.Test
    public void testSize() throws Exception {
        StringSet stringSet = new StringSetImpl();
        assertEquals(0, stringSet.size());
        stringSet.add("");
        assertEquals(1, stringSet.size());
        stringSet.add("everything");
        assertEquals(2, stringSet.size());
        stringSet.remove("");
        stringSet.add("its");
        assertEquals(2, stringSet.size());
    }

    @org.junit.Test
    public void testAdd() throws Exception {
        StringSet stringSet = new StringSetImpl();
        assertTrue(stringSet.add(""));
        assertTrue(!stringSet.add(""));
        assertTrue(stringSet.add("Everything"));
        assertTrue(!stringSet.add("Everything"));
        assertTrue(stringSet.add("everything"));
        assertTrue(stringSet.add("in"));
        assertTrue(stringSet.add("its"));
        assertTrue(stringSet.add("right"));
        assertTrue(stringSet.add("place"));
    }

    @org.junit.Test
    public void testContains() throws Exception {
        StringSet stringSet = new StringSetImpl();
        stringSet.add("");
        assertTrue(stringSet.contains(""));
        assertTrue(!stringSet.contains("in"));
        stringSet.add("everything");
        assertTrue(stringSet.contains("everything"));
        assertTrue(!stringSet.contains("Everything"));
    }

    @org.junit.Test
    public void testRemove() throws Exception {
        StringSet stringSet = new StringSetImpl();
        stringSet.add("");
        assertTrue(stringSet.contains(""));
        stringSet.add("everything");
        stringSet.add("in");
        stringSet.add("its");
        assertEquals(4, stringSet.size());
        assertTrue(stringSet.remove("in"));
        assertEquals(3, stringSet.size());
        assertTrue(!stringSet.remove("in"));
        assertTrue(stringSet.remove("its"));
    }

    @org.junit.Test
    public void testHowManyStartsWithPrefix() throws Exception {
        StringSet stringSet = new StringSetImpl();
        stringSet.add("everything");
        stringSet.add("in");
        stringSet.add("its");
        assertEquals(2, stringSet.howManyStartsWithPrefix("i"));
        assertEquals(3, stringSet.howManyStartsWithPrefix(""));
        stringSet.remove("in");
        assertEquals(1, stringSet.howManyStartsWithPrefix("i"));
    }

    @Test
    public void testSimple() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.contains("abc"));
        assertEquals(1, stringSet.size());
        assertEquals(1, stringSet.howManyStartsWithPrefix("abc"));
    }

    public static StringSet instance() {
        try {
            return (StringSet) Class.forName("ru.spbau.mit.StringSetImpl").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Error while class loading");
    }
}
