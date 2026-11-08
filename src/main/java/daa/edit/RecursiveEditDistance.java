package daa.edit;

/**
 * Version A: plain recursion, no memory.
 *
 * State:      f(i, j) = edit distance between the first i chars of a and the first j chars of b.
 * Transition: if a[i-1]==b[j-1] -> f(i-1, j-1);
 *             else 1 + min(f(i-1, j) [delete], f(i, j-1) [insert], f(i-1, j-1) [replace]).
 * Base case:  f(0, j) = j and f(i, 0) = i.
 *
 * Counter: ops = number of recursive calls (the same subproblem is counted every time it is re-solved).
 */
public class RecursiveEditDistance implements EditDistance {
    private long ops;

    @Override
    public int distance(String a, String b) {
        ops = 0;
        return rec(a, b, a.length(), b.length());
    }

    private int rec(String a, String b, int i, int j) {
        ops++;
        if (i == 0) return j;
        if (j == 0) return i;
        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return rec(a, b, i - 1, j - 1);
        }
        return 1 + Math.min(rec(a, b, i - 1, j),
                   Math.min(rec(a, b, i, j - 1),
                            rec(a, b, i - 1, j - 1)));
    }

    @Override public long getOps() { return ops; }
    @Override public String name() { return "edit_recursive"; }
}
