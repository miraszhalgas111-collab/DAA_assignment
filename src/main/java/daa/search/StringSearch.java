package daa.search;

import java.util.List;

public interface StringSearch {
    /** Start indices of all (also overlapping) occurrences, increasing. */
    List<Integer> search(String text, String pattern);

    /** Counter of the LAST search() call: number of character comparisons. */
    long getOps();

    String name();
}
