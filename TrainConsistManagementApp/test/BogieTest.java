import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;

public class BogieTest {

    @Test
    void testGrouping_BogiesGroupedByType() {
        List<Bogie> bogies = Arrays.asList(
                new Bogie("B1", "Sleeper"),
                new Bogie("B2", "Sleeper"),
                new Bogie("B3", "AC Chair")
        );

        Map<String, List<Bogie>> result =
                bogies.stream()
                        .collect(Collectors.groupingBy(Bogie::getType));

        assertEquals(2, result.get("Sleeper").size());
        assertEquals(1, result.get("AC Chair").size());
    }

    @Test
    void testGrouping_MultipleBogiesInSameGroup() {
        List<Bogie> bogies = Arrays.asList(
                new Bogie("B1", "AC Chair"),
                new Bogie("B2", "AC Chair"),
                new Bogie("B3", "AC Chair")
        );

        Map<String, List<Bogie>> result =
                bogies.stream()
                        .collect(Collectors.groupingBy(Bogie::getType));

        assertEquals(3, result.get("AC Chair").size());
    }

    @Test
    void testGrouping_DifferentBogieTypes() {
        List<Bogie> bogies = Arrays.asList(
                new Bogie("B1", "Sleeper"),
                new Bogie("B2", "First Class"),
                new Bogie("B3", "Rectangular")
        );

        Map<String, List<Bogie>> result =
                bogies.stream()
                        .collect(Collectors.groupingBy(Bogie::getType));

        assertEquals(3, result.size());
    }

    @Test
    void testGrouping_EmptyBogieList() {
        List<Bogie> bogies = new ArrayList<>();

        Map<String, List<Bogie>> result =
                bogies.stream()
                        .collect(Collectors.groupingBy(Bogie::getType));

        assertTrue(result.isEmpty());
    }

    @Test
    void testGrouping_SingleBogieCategory() {
        List<Bogie> bogies = Arrays.asList(
                new Bogie("B1", "Sleeper"),
                new Bogie("B2", "Sleeper")
        );

        Map<String, List<Bogie>> result =
                bogies.stream()
                        .collect(Collectors.groupingBy(Bogie::getType));

        assertEquals(1, result.size());
        assertTrue(result.containsKey("Sleeper"));
    }

    @Test
    void testGrouping_MapContainsCorrectKeys() {
        List<Bogie> bogies = Arrays.asList(
                new Bogie("B1", "Sleeper"),
                new Bogie("B2", "AC Chair"),
                new Bogie("B3", "First Class")
        );

        Map<String, List<Bogie>> result =
                bogies.stream()
                        .collect(Collectors.groupingBy(Bogie::getType));

        assertTrue(result.containsKey("Sleeper"));
        assertTrue(result.containsKey("AC Chair"));
        assertTrue(result.containsKey("First Class"));
    }

    @Test
    void testGrouping_GroupSizeValidation() {
        List<Bogie> bogies = Arrays.asList(
                new Bogie("B1", "Sleeper"),
                new Bogie("B2", "Sleeper"),
                new Bogie("B3", "Sleeper")
        );

        Map<String, List<Bogie>> result =
                bogies.stream()
                        .collect(Collectors.groupingBy(Bogie::getType));

        assertEquals(3, result.get("Sleeper").size());
    }

    @Test
    void testGrouping_OriginalListUnchanged() {
        List<Bogie> bogies = new ArrayList<>();
        bogies.add(new Bogie("B1", "Sleeper"));

        bogies.stream()
                .collect(Collectors.groupingBy(Bogie::getType));

        assertEquals(1, bogies.size());
    }
}