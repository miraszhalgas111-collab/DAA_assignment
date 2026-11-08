package daa.experiment;

import java.util.Random;

/** Reproducible generator: the same (seed, parameters) always give the same strings. */
public final class InputGenerator {
    public static final long SEED = 42;
    private InputGenerator() {}

    public static String random(int n, int alphabet, long seed) {
        Random r = new Random(seed);
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append((char) ('a' + r.nextInt(alphabet)));
        return sb.toString();
    }

    public static String repeat(char c, int n) {
        return String.valueOf(c).repeat(n);
    }
}
