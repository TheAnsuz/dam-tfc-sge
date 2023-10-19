/*
 */
package dev.amrv.sge;

import dev.amrv.sge.event.EventSystem;
import java.io.File;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public final class SGE {

    public final SGELogger logger;
    public final File source;

    private final ThreadGroup threadGroup;

    private final Thread initialize;
    private final Thread setup;
    private boolean started = false;
    private EventSystem eventSystem;

    protected SGE() {
        this.threadGroup = new ThreadGroup("SGE");
        this.source = new File("");
        this.logger = new SGELogger(new File("logs"));

        initialize = new Thread(threadGroup, this::initialize, "initialize");
        setup = new Thread(threadGroup, this::setup, "setup");
    }

    protected void initialize() {
        logger.info("Initializating...");
        eventSystem = new EventSystem(this, 5);
        logger.info("Created event system with {0} threads", 5);
        
        setup.start();
    }

    protected void setup() {
        logger.info("Setting up...");
    }

    protected synchronized void start() {
        if (started)
            return;

        started = true;
        initialize.start();
    }
}
