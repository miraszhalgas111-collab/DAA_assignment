package daa;

import daa.edit.*;
import daa.experiment.InputGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EditDistanceTest {
    private final List<EditDistance> all = List.of(
            new RecursiveEditDistance(), new TableEditDistance(), new TwoRowsEditDistance());

    @Test
    void referenceValues() {
        for (EditDistance e : all) {
            assertEquals(3, e.distance("kitten", "sitting"), e.name());
            assertEquals(2, e.distance("flaw", "lawn"), e.name());
            assertEquals(5, e.distance("intention", "execution"), e.name());
            assertEquals(3, e.distance("", "abc"), e.name());
            assertEquals(0, e.distance("abc", "abc"), e.name());
        }
    }

    @Test
    void emptyStrings() {
        for (EditDistance e : all) {
            assertEquals(0, e.distance("", ""), e.name());
            assertEquals(3, e.distance("abc", ""), e.name());
        }
    }

    @Test
    void threeVersionsAgreeOnRandomStrings() {
        for (int k = 0; k < 100; k++) {
            int n = 1 + k % 8, m = 1 + (k * 3) % 8;
            String a = InputGenerator.random(n, 3, 1000 + k);
            String b = InputGenerator.random(m, 3, 5000 + k);
            int expected = all.get(1).distance(a, b);
            for (EditDistance e : all) {
                assertEquals(expected, e.distance(a, b), e.name() + " on " + a + "," + b);
            }
        }
    }

    @Test
    void counterIsMeasuredInsideAlgorithm() {
        TableEditDistance t = new TableEditDistance();
        t.distance("abcd", "xyz");
        assertEquals(12, t.getOps());          // 4 * 3 cells
        TwoRowsEditDistance r = new TwoRowsEditDistance();
        r.distance("abcd", "xyz");
        assertEquals(12, r.getOps());
    }
}
