package daa.edit;

/** One interface for all three versions, so the experiment is one loop. */
public interface EditDistance {
    int distance(String a, String b);

    /** Counter of the LAST distance() call (reset at the start of each call). */
    long getOps();

    String name();
}
