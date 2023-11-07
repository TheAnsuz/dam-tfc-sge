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
    private boolean modified = false;

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
        if (!modified)
            return;

        if (!file.exists()) {
            File parent = file.getParentFile();

            if (parent != null)
                parent.mkdirs();

            file.createNewFile();
        }
        facade.store(new FileOutputStream(file), null);
        modified = false;
    }

    public synchronized void setProperty(String key, String value) {
        facade.setProperty(key, value);
        modified = true;
    }

    public String getProperty(String key) {
        return facade.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return facade.getProperty(key, defaultValue);
    }

    /**
     * Gets or creates the property with the specified value
     *
     * @param key the property to search
     * @param defaultValue a value in case there is no actual value set
     * @return the value or the default one if original does not exist
     */
    public String bringProperty(String key, String defaultValue) {
        String prop = facade.getProperty(key);

        if (prop == null) {
            prop = defaultValue;
            setProperty(key, prop);
        }

        return prop;
    }
}
