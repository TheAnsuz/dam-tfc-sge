package dev.amrv.sge;

import java.io.File;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class SGEFileSystem {

    private static File sourceFolder;
    private static File dataFolder;

    private SGEFileSystem() {
    }

    public static File getSource() {
        return sourceFolder;
    }

    static synchronized void setSourceFolder(File sourceFolder) {
        SGEFileSystem.sourceFolder = sourceFolder;
    }

    public static File getSourceFile(String... path) {
        return new File(sourceFolder, String.join(File.separator, path));
    }

    static synchronized void setDataFolder(File dataFolder) {
        SGEFileSystem.dataFolder = dataFolder;
    }

    public static File getDataFile(String... path) {
        return new File(dataFolder, String.join(File.separator, path));
    }

    public static File getFile(String... path) {
        return new File(String.join(File.separator, path));
    }

}
