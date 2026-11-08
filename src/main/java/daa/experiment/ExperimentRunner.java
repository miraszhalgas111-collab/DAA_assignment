package daa.experiment;

import daa.edit.*;
import daa.search.*;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Runs the whole experiment and writes results/results.csv.
 * Usage: mvn -q compile exec:java    (optional arg: output path)
 * Columns: algorithm,input,n,m,time_ms,ops,result
 *   n = length of the first string / text, m = length of the second string / pattern,
 *   time_ms = median of RUNS runs, ops = counter inside the algorithm, result = distance or number of matches.
 */
public class ExperimentRunner {
    static final int RUNS = 5;

    // Recursive version is exponential, so it gets small sizes only.
    static final int[] REC_SIZES = {3, 6, 9, 11, 12};
    static final int[] DP_SIZES = {200, 400, 800, 1600, 3200};
    static final int[] TEXT_SIZES = {10_000, 20_000, 40_000, 80_000, 160_000};
    static final int PATTERN_LEN = 100;

    public static void main(String[] args) throws Exception {
        Path out = Path.of(args.length > 0 ? args[0] : "results/results.csv");
        List<EditDistance> edits = List.of(new RecursiveEditDistance(), new TableEditDistance(), new TwoRowsEditDistance());
        List<StringSearch> searches = List.of(new NaiveSearch(), new KmpSearch());

        warmUp(edits, searches);

        try (CsvWriter csv = new CsvWriter(out, "algorithm,input,n,m,time_ms,ops,result")) {
            // ----- Edit Distance: one loop for all three versions -----
            for (EditDistance ed : edits) {
                int[] sizes = ed instanceof RecursiveEditDistance ? REC_SIZES : DP_SIZES;
                for (String type : new String[]{"random", "disjoint", "identical"}) {
                    for (int n : sizes) {
                        String a, b;
                        switch (type) {
                            case "random" -> { a = InputGenerator.random(n, 4, InputGenerator.SEED + n);
                                               b = InputGenerator.random(n, 4, InputGenerator.SEED + 7L * n + 1); }
                            case "disjoint" -> { a = InputGenerator.repeat('a', n);   // no common chars: recursion never takes the cheap branch
                                                 b = InputGenerator.repeat('b', n); }
                            default -> { a = InputGenerator.random(n, 4, InputGenerator.SEED + n); b = a; }
                        }
                        final String fa = a, fb = b;
                        int[] res = new int[1];
                        double t = medianMs(() -> res[0] = ed.distance(fa, fb));
                        csv.row(ed.name(), type, n, n, String.format(java.util.Locale.ROOT, "%.4f", t), ed.getOps(), res[0]);
                        System.out.printf("%-15s %-9s n=%-6d %10.3f ms ops=%d%n", ed.name(), type, n, t, ed.getOps());
                    }
                }
            }
            // ----- String search: Naive vs KMP -----
            for (StringSearch ss : searches) {
                for (String type : new String[]{"random", "worst", "all_match"}) {
                    for (int n : TEXT_SIZES) {
                        String text, pat;
                        switch (type) {
                            case "random" -> { text = InputGenerator.random(n, 4, InputGenerator.SEED + n);
                                               pat = InputGenerator.random(20, 4, InputGenerator.SEED + 99); }
                            case "worst" -> { text = InputGenerator.repeat('a', n);          // a^n vs a^(m-1)b : naive re-reads m-1 chars at every start
                                              pat = InputGenerator.repeat('a', PATTERN_LEN - 1) + "b"; }
                            default -> { text = InputGenerator.repeat('a', n);               // a^n vs a^m : n-m+1 overlapping matches
                                         pat = InputGenerator.repeat('a', PATTERN_LEN); }
                        }
                        final String ft = text, fp = pat;
                        int[] res = new int[1];
                        double t = medianMs(() -> res[0] = ss.search(ft, fp).size());
                        csv.row(ss.name(), type, n, pat.length(), String.format(java.util.Locale.ROOT, "%.4f", t), ss.getOps(), res[0]);
                        System.out.printf("%-15s %-9s n=%-7d %10.3f ms ops=%d matches=%d%n", ss.name(), type, n, t, ss.getOps(), res[0]);
                    }
                }
            }
        }
        System.out.println("Written: " + out);
    }

    private static double medianMs(Runnable r) {
        double[] t = new double[RUNS];
        for (int i = 0; i < RUNS; i++) {
            long s = System.nanoTime();
            r.run();
            t[i] = (System.nanoTime() - s) / 1e6;
        }
        Arrays.sort(t);
        return t[RUNS / 2];
    }

    private static void warmUp(List<EditDistance> edits, List<StringSearch> searches) {
        String a = InputGenerator.random(8, 4, 1), b = InputGenerator.random(8, 4, 2);
        String t = InputGenerator.random(2000, 4, 3);
        for (int k = 0; k < 20; k++) {
            for (EditDistance e : edits) e.distance(a, b);
            for (StringSearch s : searches) s.search(t, "abca");
        }
    }
}
