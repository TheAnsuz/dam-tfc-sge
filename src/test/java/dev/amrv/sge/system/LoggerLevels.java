package dev.amrv.sge.system;

import dev.amrv.sge.SGELogger;
import java.io.File;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class LoggerLevels {

    public static void main(String[] args) {
        SGELogger logger = new SGELogger(new File("test/logs")) {
        };

        logger.setEnabled(SGELogger.LEVEL_DEBUG | SGELogger.LEVEL_ACTION, true);

        System.out.println("Action: " + logger.isEnabled(SGELogger.LEVEL_ACTION));
        System.out.println("Config: " + logger.isEnabled(SGELogger.LEVEL_CONFIG));
        System.out.println("Debug:  " + logger.isEnabled(SGELogger.LEVEL_DEBUG));
        System.out.println("Error:  " + logger.isEnabled(SGELogger.LEVEL_ERROR));
        System.out.println("Info:   " + logger.isEnabled(SGELogger.LEVEL_INFO));
        System.out.println("Warn:   " + logger.isEnabled(SGELogger.LEVEL_WARN));

    }
}
