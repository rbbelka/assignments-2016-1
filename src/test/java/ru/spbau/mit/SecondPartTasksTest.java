package ru.spbau.mit;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static ru.spbau.mit.SecondPartTasks.*;

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
        for (int i = 0; i < 15; i++) {
            assertEquals(Math.PI / 4, piDividedBy4(), 1e-3);
        }
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> compositions = new HashMap<>();
        assertEquals("", findPrinter(compositions));

        compositions.put("Author0", Collections.emptyList());
        assertEquals("Author0", findPrinter(compositions));

        compositions.put("Author1", asList("Content11", "Content12"));
        compositions.put("Author2", asList("Content21", "Content22", "Content23"));
        compositions.put("Author3", asList("Content31", "C32", "C33"));
        assertEquals("Author2", findPrinter(compositions));
    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> retailer0 = new HashMap<>();
        Map<String, Integer> retailer1 = new HashMap<>();
        Map<String, Integer> retailer2 = new HashMap<>();
        Map<String, Integer> retailer3 = new HashMap<>();
        retailer1.put("goods1", 21);
        retailer2.put("goods2", 16);
        retailer3.put("goods3", 73);
        retailer1.put("goods4", 152);
        retailer2.put("goods4", 792);
        retailer3.put("goods4", 371);
        retailer2.put("goods5", 47);
        retailer3.put("goods5", 10);

        Map<String, Integer> expected
                = ImmutableMap.of("goods1", 21, "goods2", 16, "goods3", 73, "goods4", 1315, "goods5", 57);
        assertEquals(expected, calculateGlobalOrder(asList(retailer1,retailer2,retailer3)));
    }
}
