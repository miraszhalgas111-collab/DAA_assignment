package daa.edit;

/**
 * Version C: two rows only.
 *
 * State:      prev[j] = dp[i-1][j], cur[j] = dp[i][j] (row i of the table; older rows are not needed).
 * Transition: cur[j] = prev[j-1] if a[i-1]==b[j-1],
 *             else 1 + min(prev[j], cur[j-1], prev[j-1]); then swap(prev, cur).
 * Base case:  prev[j] = j for row 0, and cur[0] = i for every row i.
 *
 * Time O(n*m), extra memory O(m).
 * Counter: ops = number of cells computed (same meaning as in the table version).
 */
public class TwoRowsEditDistance implements EditDistance {
    private long ops;

    @Override
    public int distance(String a, String b) {
        ops = 0;
        int n = a.length(), m = b.length();
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= n; i++) {
            cur[0] = i;
            for (int j = 1; j <= m; j++) {
                ops++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    cur[j] = prev[j - 1];
                } else {
                    cur[j] = 1 + Math.min(prev[j], Math.min(cur[j - 1], prev[j - 1]));
                }
            }
            int[] tmp = prev; prev = cur; cur = tmp;
        }
        return prev[m];
    }

    @Override public long getOps() { return ops; }
    @Override public String name() { return "edit_two_rows"; }
}
