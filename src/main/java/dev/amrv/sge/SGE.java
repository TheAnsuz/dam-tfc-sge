/*
 */
package dev.amrv.sge;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import dev.amrv.sge.auth.UserCredentials;
import dev.amrv.sge.event.EventSystem;
import dev.amrv.sge.event.impl.SGEInitializeEvent;
import dev.amrv.sge.event.impl.SGESetupEvent;
import dev.amrv.sge.event.impl.SGEUserChangeEvent;
import dev.amrv.sge.io.PropertiesFile;
import dev.amrv.sge.module.Module;
import dev.amrv.sge.window.SGENotifier;
import dev.amrv.sge.window.SGEWindow;
import dev.amrv.sge.window.UserCredentialsDialog;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public final class SGE {

    private final List<Module> modules = new ArrayList<>();

    public final SGELogger logger;

    private final ThreadGroup threadGroup;

    private final Thread initialize;
    private final Thread setup;
    private final Thread shutdown;
    private boolean started = false;
    private EventSystem eventSystem;
    private final PropertiesFile properties;
    private UserCredentials userCredentials;
    private SGEWindow window;

    protected SGE() {
        this.threadGroup = new ThreadGroup("SGE");
        SGEFileSystem.setSourceFolder(new File(new File("").getAbsolutePath()));
        SGEFileSystem.setDataFolder(new File("data"));

        this.logger = new SGELogger(new File("logs"));
        this.properties = new PropertiesFile(SGEFileSystem.getDataFile("sge.properties"));

        initialize = new Thread(threadGroup, this::initialize, "initialize");
        setup = new Thread(threadGroup, this::setup, "setup");
        shutdown = new Thread(threadGroup, this::shutdown, "shutdown");
    }

    public PropertiesFile getProperties() {
        return properties;
    }

    public EventSystem getEventSystem() {
        return eventSystem;
    }

    public void addModule(Module module) {
        modules.add(module);
    }

    private void initialize() {
        logger.info("Initializating...");

        try {
            userCredentials = UserCredentials.loadAnonymous();
        } catch (IOException ex) {
            userCredentials = UserCredentials.anonymous();
            logger.error("Can't load anonymous user", ex);
        }

        Runtime.getRuntime().addShutdownHook(shutdown);
        logger.info("Creating event system with {0} threads", 5);
        eventSystem = new EventSystem(this, 5);

        try {
            logger.info("Loading properties {0}", properties.getFile().getPath());
            this.properties.read();
        } catch (IOException ex) {
            logger.error(ex);
        }

        logger.info("Initializating window...");
        window = new SGEWindow(this);

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

        String lafName = properties.getProperty("sge.window.lookAndFeel");
        try {
            if (lafName == null) {
                logger.info("No Look and feel found at window.lookAndFeel");
                properties.setProperty("sge.window.lookAndFeel", UIManager.getLookAndFeel().getName());
            } else {
                logger.info("Setting app look and feel to \"{0}\"", lafName);
                UIManager.setLookAndFeel(feelins.get(lafName));
            }
        } catch (NullPointerException | UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            logger.error(ex);
        }

        getEventSystem().queueEvent(new SGESetupEvent(this));
        logger.info("Finished setting up");

        int attempts = 1;
        while (true) {
            logger.info("Logging attempt {0}", attempts);
            UserCredentialsDialog dialog = new UserCredentialsDialog(null);
            dialog.setVisible(true);

            if (dialog.wasCanceled()) {
                logger.info("Login canceled");
                System.exit(0);
                return;
            }

            UserCredentials credentials = dialog.generateCredentials();

            if (credentials.isValid()) {
                setUser(credentials);
                break;
            } else {
                logger.warn("Tried to login with {0}", credentials.getUsername());
                SGENotifier.displayError(dialog, "Login error", "User does not exist");
            }
        }

        logger.info("Loading modules...");
        for (Module module : modules) {
            logger.info("Loading module: " + module.getName());
            module.load(this);
            window.addModule(module);
        }

        logger.info("Opening UI");
        window.setTitle(getUser().getUsername());
        window.setVisible(true);
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
        getEventSystem().queueEvent(new SGEUserChangeEvent(userCredentials, credentials));
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

        for (Module module : modules) {
            logger.info("Unloading module: " + module.getName());
            module.unload();
        }

        try {
            logger.info("Saving configuration");
            properties.save();
        } catch (IOException ex) {
            logger.error(ex);
        }

        try {
            logger.info("Saving permissions");
            userCredentials.savePermissions();
        } catch (IOException ex) {
            logger.error(ex);
        }

        logger.info("Exited correctly");
    }
}
