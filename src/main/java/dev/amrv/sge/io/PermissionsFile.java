package dev.amrv.sge.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

            String line;

            do {
                line = reader.readLine();

                if (line == null)
                    break;

                content.add(decompileString(line));
            } while (true);

        }
    }

    public List<String> content() {
        return content;
    }

    protected String compileString(String line) {
        // Example cipher
        String result = "";

        int i = 0;

        for (char c : line.toCharArray())
            result += (char)(c + (++i % 4));

        return result;
    }

    protected String decompileString(String line) {
        String result = "";

        int i = 0;

        for (char c : line.toCharArray())
            result += (char)(c - (++i % 4));

        return result;
    }
}
