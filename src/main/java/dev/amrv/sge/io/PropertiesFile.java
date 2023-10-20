package dev.amrv.sge.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class PropertiesFile {

    private final java.util.Properties facade;
    private final File file;

    public PropertiesFile(String file) {
        this(new File(file));
    }

    public PropertiesFile(File file) {
        this.facade = new java.util.Properties();
        this.file = file;
    }

    public File getFile() {
        return file;
    }
    
    public void read() throws IOException {
        if (file.canRead())
            facade.load(new FileInputStream(file));
    }

    public void save() throws IOException {
        if (!file.exists()) {
            File parent = file.getParentFile();
            
            if (parent != null)
                parent.mkdirs();

            file.createNewFile();
        }
        facade.store(new FileOutputStream(file), null);
    }

    public synchronized void setProperty(String key, String value) {
        facade.setProperty(key, value);
    }

    public String getProperty(String key) {
        return facade.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return facade.getProperty(key, defaultValue);
    }

}
