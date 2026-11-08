package daa;

import daa.experiment.InputGenerator;
import daa.search.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchTest {
    private final List<StringSearch> all = List.of(new NaiveSearch(), new KmpSearch());

    @Test
    void referenceMatches() {
        for (StringSearch s : all) {
            assertEquals(List.of(10), s.search("ABABDABACDABABCABAB", "ABABCABAB"), s.name());
            assertEquals(List.of(0, 1, 2, 3), s.search("AAAAA", "AA"), s.name());   // overlapping
            assertEquals(List.of(0, 2, 4), s.search("ABABABA", "ABA"), s.name());   // overlapping
        }
    }

    @Test
    void noMatchGivesEmptyList() {
        for (StringSearch s : all) assertEquals(List.of(), s.search("ABCDEF", "XY"), s.name());
    }

    @Test
    void naiveAndKmpAgreeOnRandomTexts() {
        NaiveSearch naive = new NaiveSearch();
        KmpSearch kmp = new KmpSearch();
        for (int k = 0; k < 100; k++) {
            String text = InputGenerator.random(50 + k, 2, 100 + k);   // alphabet 2 -> many overlaps
            String pat = InputGenerator.random(1 + k % 5, 2, 900 + k);
            assertEquals(naive.search(text, pat), kmp.search(text, pat), "text=" + text + " pat=" + pat);
        }
    }

    @Test
    void kmpNeverMakesMoreThanAboutTwoNComparisons() {
        KmpSearch kmp = new KmpSearch();
        String text = "a".repeat(10_000);
        kmp.search(text, "a".repeat(99) + "b");
        // LPS (m) + search (<= 2n): no backward moves in the text
        assertEquals(true, kmp.getOps() <= 2L * text.length() + 100);
    }
}
