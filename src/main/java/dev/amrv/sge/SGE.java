/*
 */
package dev.amrv.sge;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import dev.amrv.sge.auth.UserCredentials;
import dev.amrv.sge.bbdd.Database;
import dev.amrv.sge.event.EventSystem;
import dev.amrv.sge.event.impl.SGEInitializeEvent;
import dev.amrv.sge.event.impl.SGEModuleChange;
import dev.amrv.sge.event.impl.SGESetupEvent;
import dev.amrv.sge.event.impl.SGEUserChangeEvent;
import dev.amrv.sge.io.PropertiesFile;
import dev.amrv.sge.module.Module;
import dev.amrv.sge.window.SGENotifier;
import dev.amrv.sge.window.SGEWindow;
import dev.amrv.sge.window.UserCredentialsDialog;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public final class SGE {

    private final static String USER_LAST_NAME = "sge.user.last";

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
    private Database database;

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
        eventSystem.start();

        try {
            logger.info("Loading properties {0}", properties.getFile().getPath());
            this.properties.read();
        } catch (IOException ex) {
            logger.error(ex);
        }

        logger.info("Initializating database...");
        try {
            database = new Database("bbdd");
        } catch (SQLException ex) {
            logger.error(ex);
            System.exit(1);
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
        FlatLightLaf light = new FlatLightLaf();
        FlatDarkLaf dark = new FlatDarkLaf();

        UIManager.installLookAndFeel(light.getName(), light.getClass().getCanonicalName());
        UIManager.installLookAndFeel(dark.getName(), dark.getClass().getCanonicalName());

        String lafName = properties.getProperty("sge.window.lookAndFeel");

        LookAndFeelInfo toChange = null;
        int index = 0;
        for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            logger.debug("Registered laf {0} with index {1}", laf.getName(), index);

            if (lafName.equals(laf.getName()) || laf.getClassName().equals(lafName)) {
                toChange = laf;
            }
        }

        logger.info("Found {0} look and feels", UIManager.getInstalledLookAndFeels().length);

        try {
            setLookAndFeel(toChange);
        } catch (Exception ex) {
            logger.error(ex);
        }

        getEventSystem().queueEvent(new SGESetupEvent(this));
        logger.info("Finished setting up");

        String lastUsername = properties.getProperty(USER_LAST_NAME, "");

        int attempts = 1;

        while (true) {
            logger.info("Logging attempt {0}", attempts);
            UserCredentialsDialog dialog = new UserCredentialsDialog(null);
            dialog.setUsername(lastUsername);
            dialog.setVisible(true);

            if (dialog.wasCanceled()) {
                logger.info("Login canceled");
                System.exit(0);
                return;
            }

            UserCredentials credentials = dialog.generateCredentials();

            if (credentials.isValid()) {
                userCredentials = null;
                setUser(credentials);
                break;
            } else {
                logger.warn("Tried to login with {0}", credentials.getUsername());
                SGENotifier.displayError(dialog, "Login error", "User does not exist");
            }
        }

        logger.info(
                "Loading modules...");
        for (Module module : modules) {
            logger.info("Loading module: " + module.getName());
            if (module.load(this)) {
                window.addModule(module);
            } else {
                logger.error("There was an error loading module {0}", module.getName());
            }
        }

        logger.info("Opening UI");

        window.setTitle(getUser().getUsername());
        window.setVisible(
                true);
    }

    public void setLookAndFeel(LookAndFeelInfo info) throws Exception {
        try {
            UIManager.setLookAndFeel(info.getClassName());
            properties.setProperty("sge.window.lookAndFeel", info.getName());

            SwingUtilities.updateComponentTreeUI(window);

            for (Module module : modules)
                if (module.isLoaded()) {
                    SwingUtilities.updateComponentTreeUI(module.getPanel());
                }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            throw new Exception(ex);
        }
    }

    public Database getDatabase() {
        return database;
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
        properties.setProperty(USER_LAST_NAME, credentials.getUsername());
        getEventSystem().queueEvent(new SGEUserChangeEvent(userCredentials, credentials));
    }

    private Module displayModule;

    public synchronized void setModuleOnDisplay(Module module) {
        getEventSystem().queueEvent(new SGEModuleChange(displayModule, module));
        displayModule = module;
        window.setActivePanel(module.getPanel());
        module.onAppear();
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
