package daa.edit;

/**
 * Version B: bottom-up table (n+1) x (m+1).
 *
 * State:      dp[i][j] = edit distance between a[0..i) and b[0..j).
 * Transition: dp[i][j] = dp[i-1][j-1] if a[i-1]==b[j-1],
 *             else 1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]).
 * Base case:  dp[i][0] = i, dp[0][j] = j.
 *
 * Time O(n*m), memory O(n*m).
 * Counter: ops = number of table cells computed (cells with i>=1 and j>=1).
 */
public class TableEditDistance implements EditDistance {
    private long ops;

    @Override
    public int distance(String a, String b) {
        ops = 0;
        int n = a.length(), m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                ops++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j],
                                   Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }
        return dp[n][m];
    }

    @Override public long getOps() { return ops; }
    @Override public String name() { return "edit_table"; }
}
