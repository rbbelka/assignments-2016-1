package ru.spbau.mit;

import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static ru.spbau.mit.SecondPartTasks.findQuotes;
import static ru.spbau.mit.SecondPartTasks.piDividedBy4;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() throws IOException {
        assertEquals (
                findQuotes(Collections.emptyList(), "a"),
                Collections.emptyList());

        List<String> paths = asList("src/test/resources/lotus.txt",
                                            "src/test/resources/reckoner.txt",
                                            "src/test/resources/sleep.txt");

        assertEquals (
                findQuotes(paths, "all"),
                asList(
                    "And all I want is the moon upon a stick",
                    "Just to feed your fast ballooning head",
                    "Because all I want is the moon upon a stick",
                    "Just to feed my fast ballooning head",
                    "dedicated to all you",
                    "all human beings",
                    "I'm gonna go to sleep let this wash all over me"
                ));

        assertEquals (
                findQuotes(paths, "can't"),
                asList(
                    "I can't kick the habit",
                    "I can't kick the habit",
                    "you can't take it with you"
                ));
    }

    @Test
    public void testPiDividedBy4() {
        assertEquals(Math.PI / 4, piDividedBy4(), 1e-3);
    }

    @Test
    public void testFindPrinter() {
        fail();
    }

    @Test
    public void testCalculateGlobalOrder() {
        fail();
    }
}
