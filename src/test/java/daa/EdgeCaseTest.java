package daa;

import daa.search.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EdgeCaseTest {
    private final List<StringSearch> all = List.of(new NaiveSearch(), new KmpSearch());

    @Test
    void emptyPatternThrows() {
        for (StringSearch s : all) {
            assertThrows(IllegalArgumentException.class, () -> s.search("abc", ""), s.name());
        }
    }

    @Test
    void patternLongerThanTextGivesEmptyList() {
        for (StringSearch s : all) assertEquals(List.of(), s.search("ab", "abc"), s.name());
    }
}
