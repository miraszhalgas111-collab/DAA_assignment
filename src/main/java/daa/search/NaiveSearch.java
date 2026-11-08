package daa.search;

import java.util.ArrayList;
import java.util.List;

/** Naive search: try every start, compare char by char. Counter: ops = character comparisons. */
public class NaiveSearch implements StringSearch {
    private long ops;

    @Override
    public List<Integer> search(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("pattern must not be empty");
        }
        ops = 0;
        List<Integer> res = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        for (int s = 0; s + m <= n; s++) {
            int j = 0;
            while (j < m) {
                ops++;
                if (text.charAt(s + j) != pattern.charAt(j)) break;
                j++;
            }
            if (j == m) res.add(s);
        }
        return res;
    }

    @Override public long getOps() { return ops; }
    @Override public String name() { return "search_naive"; }
}
