package daa.experiment;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class CsvWriter implements AutoCloseable {
    private final PrintWriter out;

    public CsvWriter(Path path, String header) throws IOException {
        if (path.getParent() != null) Files.createDirectories(path.getParent());
        out = new PrintWriter(Files.newBufferedWriter(path));
        out.println(header);
    }

    public void row(Object... cols) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(cols[i]);
        }
        out.println(sb);
    }

    @Override public void close() { out.close(); }
}
