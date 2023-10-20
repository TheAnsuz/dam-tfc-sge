package dev.amrv.sge.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class PermissionsFile {

    private final File file;
    private final List<String> content = new ArrayList<>();

    public PermissionsFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void save() throws IOException {

        if (!file.exists()) {
            File parent = file.getParentFile();

            if (parent != null)
                parent.mkdirs();

            file.createNewFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (String line : content)
                writer.append(compileString(line)).append(System.lineSeparator());
        }

    }

    public void read() throws IOException {
        if (!file.canRead())
            return;

        content.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            while (reader.ready())
                content.add(decompileString(reader.readLine()));
        }
    }

    public List<String> content() {
        return content;
    }

    protected String compileString(String line) {
        return line;
    }

    protected String decompileString(String line) {
        return line;
    }
}
