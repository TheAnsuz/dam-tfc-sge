package dev.amrv.sge.io;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class CSVWriter implements Closeable {

    private final BufferedWriter writer;
    private final String splitter = ";";

    public CSVWriter(File file) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(file));
    }

    public CSVWriter skip(int amount) throws IOException {
        for (int i = 0; i < amount; i++)
            writer.append(splitter);
        return this;
    }

    public CSVWriter addLine(String... parts) throws IOException {
        for (String part : parts)
            writer.append(part).append(splitter);
        writer.newLine();
        return this;
    }

    public CSVWriter add(String... parts) throws IOException {
        for (String part : parts)
            writer.append(part).append(splitter);
        return this;
    }

    public CSVWriter nextLine() throws IOException {
        writer.newLine();
        return this;
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

}
