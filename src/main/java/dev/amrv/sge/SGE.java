/*
 */
package dev.amrv.sge;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import dev.amrv.sge.auth.UserCredentials;
import dev.amrv.sge.event.EventSystem;
import dev.amrv.sge.event.impl.SGEInitializeEvent;
import dev.amrv.sge.event.impl.SGESetupEvent;
import dev.amrv.sge.io.PropertiesFile;
import dev.amrv.sge.window.UserCredentialsWindow;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public final class SGE {

    public final SGELogger logger;

    private final ThreadGroup threadGroup;

    private final Thread initialize;
    private final Thread setup;
    private final Thread shutdown;
    private boolean started = false;
    private EventSystem eventSystem;
    private final PropertiesFile properties;
    private UserCredentials userCredentials;

    protected SGE() {
        this.threadGroup = new ThreadGroup("SGE");
        SGEFileSystem.setSourceFolder(new File(""));
        SGEFileSystem.setDataFolder(new File("data"));

        this.logger = new SGELogger(new File("logs"));
        this.properties = new PropertiesFile(SGEFileSystem.getDataFile("sge.properties"));

        initialize = new Thread(threadGroup, this::initialize, "initialize");
        setup = new Thread(threadGroup, this::setup, "setup");
        shutdown = new Thread(threadGroup, this::shutdown, "shutdown");
    }

    public EventSystem getEventSystem() {
        return eventSystem;
    }

    private void initialize() {
        logger.info("Initializating...");

        Runtime.getRuntime().addShutdownHook(shutdown);

        logger.info("Creating event system with {0} threads", 5);
        eventSystem = new EventSystem(this, 5);

        try {
            logger.info("Loading properties {0}", properties.getFile().getPath());
            this.properties.read();
        } catch (IOException ex) {
            logger.error(ex);
        }

        getEventSystem().queueEvent(new SGEInitializeEvent(this));
        logger.info("Finished initializating");
        setup.start();
    }

    private void setup() {
        logger.info("Setting up...");

        logger.info("Loading look and feels");
        final Map<String, String> feelins = new HashMap<>();
        FlatLightLaf light = new FlatLightLaf();
        FlatDarkLaf dark = new FlatDarkLaf();

        feelins.put(light.getName(), light.getClass().toString());
        logger.debug("Registered laf {0} with index {1}", light.getName(), feelins.size());
        
        feelins.put(dark.getName(), dark.getClass().toString());
        logger.debug("Registered laf {0} with index {1}", dark.getName(), feelins.size());
        
        for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            feelins.put(laf.getName(), laf.getClassName());
            logger.debug("Registered laf {0} with index {1}", laf.getName(), feelins.size());
        }

        logger.info("Found {0} look and feels", feelins.size());

        String lafName = properties.getProperty("window.lookAndFeel");
        try {
            if (lafName == null) {
                logger.info("No Look and feel found at window.lookAndFeel");
                properties.setProperty("window.lookAndFeel", UIManager.getLookAndFeel().getName());
            } else {
                logger.info("Setting app look and feel to \"{0}\"", lafName);
                UIManager.setLookAndFeel(feelins.get(lafName));
            }
        } catch (NullPointerException | UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            logger.error(ex);
        }

        getEventSystem().queueEvent(new SGESetupEvent(this));
        logger.info("Finished setting up");
        setUser(UserCredentialsWindow.request());
    }

    public synchronized void setUser(UserCredentials credentials) {
        if (credentials == null)
            return;

        try {
            if (userCredentials != null)
                userCredentials.savePermissions();
        } catch (IOException ex) {
            logger.error(ex);
        }

        try {
            credentials.loadPermissions();
        } catch (IOException ex) {
            logger.error(ex);
        }

        logger.info("Changed user to {0}", credentials.getUsername());
        this.userCredentials = credentials;
    }

    public UserCredentials getUser() {
        return userCredentials;
    }

    protected synchronized void start() {
        if (started)
            return;

        started = true;
        initialize.start();
    }

    private synchronized void shutdown() {
        logger.info("Shutting down...");
        try {
            properties.save();
        } catch (IOException ex) {
            logger.error(ex);
        }
        logger.info("Closed");
    }
}
