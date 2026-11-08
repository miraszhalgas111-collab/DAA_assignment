package daa.search;

import java.util.ArrayList;
import java.util.List;

/**
 * KMP search. lps[i] = length of the longest proper prefix of p[0..i] that is also its suffix.
 * The text index i never moves backwards: on a mismatch only the pattern index j falls back to lps[j-1].
 * Counter: ops = character comparisons (LPS construction + search).
 */
public class KmpSearch implements StringSearch {
    private long ops;

    /** Public so that tests can call it. */
    public static int[] lps(String p) {
        return buildLps(p, new long[1]);
    }

    private static int[] buildLps(String p, long[] counter) {
        int m = p.length();
        int[] lps = new int[m];
        int len = 0, i = 1;
        while (i < m) {
            counter[0]++;
            if (p.charAt(i) == p.charAt(len)) {
                lps[i++] = ++len;
            } else if (len > 0) {
                len = lps[len - 1];
            } else {
                lps[i++] = 0;
            }
        }
        return lps;
    }

    @Override
    public List<Integer> search(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("pattern must not be empty");
        }
        long[] c = new long[1];
        List<Integer> res = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m > n) { ops = 0; return res; }
        int[] lps = buildLps(pattern, c);
        int i = 0, j = 0;
        while (i < n) {
            c[0]++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++; j++;
                if (j == m) {
                    res.add(i - m);
                    j = lps[j - 1];
                }
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        ops = c[0];
        return res;
    }

    @Override public long getOps() { return ops; }
    @Override public String name() { return "search_kmp"; }
}
