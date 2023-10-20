package dev.amrv.sge;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class SGELogger {

    public static final int LEVEL_ERROR = 0x1000000;
    public static final int LEVEL_WARN = 0x100000;
    public static final int LEVEL_INFO = 0x10000;
    public static final int LEVEL_DEBUG = 0x1000;
    public static final int LEVEL_CONFIG = 0x100;
    public static final int LEVEL_ACTION = 0x10;

    private static final String ERROR = "ERROR";
    private static final String WARN = "WARN";
    private static final String INFO = "INFO";
    private static final String DEBUG = "DFBUG";
    private static final String CONFIG = "CONFIG";

    private static final DateTimeFormatter LOG_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
    private static final DateTimeFormatter LOG_FILE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy_hh-mm-ss");
    private final File folder;
    private PrintStream printer;
    public int level = 0x000000;

    protected SGELogger(File logFolder) {
        this.folder = logFolder;

        if (logFolder.isDirectory() || logFolder.mkdirs()) {
            final File logFile = new File(folder, "sge_" + LocalDateTime.now().format(LOG_FILE_FORMATTER) + ".log");

            if (!logFile.exists())
                try {
                logFile.createNewFile();

                printer = new PrintStream(logFile);
            } catch (IOException ex) {
                warn("Can't create log file {0}", logFile.getName());
            }

        }
    }

    public void setEnabled(int level, boolean value) {
        this.level |= level;
    }

    public boolean isEnabled(int level) {
        return (this.level & level) > 0;
    }

    public void action(String who, String message, Object... params) {
        if (!isEnabled(LEVEL_ACTION))
            return;
        writeLog(format(who, message, params));
    }

    public void error(Throwable exception) {
        if (!isEnabled(LEVEL_ERROR))
            return;
        writeLog(format(ERROR, exception.toString(), new Object[0]));
    }

    public void error(String message, Object... params) {
        if (!isEnabled(LEVEL_ERROR))
            return;
        writeLog(format(ERROR, message, params));
    }

    public void warn(Throwable exception) {
        if (!isEnabled(LEVEL_WARN))
            return;
        writeLog(format(WARN, exception.toString(), new Object[0]));
    }

    public void warn(String message, Object... params) {
        if (!isEnabled(LEVEL_WARN))
            return;
        writeLog(format(WARN, message, params));
    }

    public void info(Throwable exception) {
        if (!isEnabled(LEVEL_INFO))
            return;
        writeLog(format(INFO, exception.toString(), new Object[0]));
    }

    public void info(String message, Object... params) {
        if (!isEnabled(LEVEL_INFO))
            return;
        writeLog(format(INFO, message, params));
    }

    public void debug(Throwable exception) {
        if (!isEnabled(LEVEL_DEBUG))
            return;
        writeLog(format(DEBUG, exception.toString(), new Object[0]));
    }

    public void debug(String message, Object... params) {
        if (!isEnabled(LEVEL_DEBUG))
            return;
        writeLog(format(DEBUG, message, params));
    }

    public void config(Throwable exception) {
        if (!isEnabled(LEVEL_CONFIG))
            return;
        writeLog(format(CONFIG, exception.toString(), new Object[0]));
    }

    public void config(String message, Object... params) {
        if (!isEnabled(LEVEL_CONFIG))
            return;
        writeLog(format(CONFIG, message, params));
    }

    protected void writeLog(String log) {
        System.out.println(log);
        printer.println(log);
    }

    protected String format(String log, String message, Object[] params) {
        StringBuilder builder = new StringBuilder(message);
        formatParameters(builder, params);
        return formatMessage(log, builder);
    }

    protected String formatMessage(String logType, StringBuilder message) {
        message.insert(0, getTimestamp() + " [" + Thread.currentThread().getThreadGroup().getName() + ":" + Thread.currentThread().getName() + "] [" + logType + "]: ");
        return message.toString();
    }

    protected void formatParameters(StringBuilder message, Object[] params) {
        for (int i = 0; i < params.length; i++) {

            int index = message.indexOf("{" + i + "}", 0);

            if (index == 0 || message.charAt(index - 1) != '\\')
                message.replace(index, index + 3, params[i].toString());

        }
    }

    protected String getTimestamp() {
        return LocalDateTime.now().format(LOG_FORMATTER);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (printer != null)
            printer.close();
    }
}
