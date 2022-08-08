package homeprime.core.logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import homeprime.agent.config.enums.LoggerType;
import homeprime.core.exception.ThingException;
import homeprime.core.model.readers.config.ConfigurationReader;

/**
 * Main logger.
 *
 * @author Milan Ramljak
 *
 */
public class IoTLogger {

    private static IoTLogger instance = null;
    private static LoggerType activeLoggerType = LoggerType.Standard;

    /**
     *
     * Hidden constructor.
     */
    private IoTLogger() {
    }

    private void writeToLog(final String loggerFilePath, final String logEntry) throws IOException {

        Path path = Paths.get(loggerFilePath);
        byte[] strToBytes = (logEntry + "\n").getBytes();

        Files.write(path, strToBytes, StandardOpenOption.APPEND);
    }

    public static IoTLogger getInstance() {
        if (instance == null) {
            instance = new IoTLogger();
            try {
                activeLoggerType = ConfigurationReader.getConfiguration().getAgent().getLoggerType();
            } catch (ThingException e) {
                System.err.println("IoTLogger.getInstance() Failed to read active logger type!");
            }
        }
        return instance;
    }

    private void log(final String logLevel, final String log) {
        switch (activeLoggerType) {
            case File:
                try {
                    writeToLog(ConfigurationReader.getConfiguration().getAgent().getLoggerFilePath(),
                            logLevel + " " + log);
                } catch (IOException e) {
                    System.err.println("IoTLogger.log() Failed to write to log file!");
                } catch (ThingException e) {
                    System.err.println("IoTLogger.log() Failed to read logger file type!");
                }
                break;
            case Standard:
                System.out.println(logLevel + " " + log);
                break;
            case Void:
                // Do not log anything
                break;
            default:
                break;
        }
    }

    public void info(final String message) {
        log("INFO", message);
    }

    public void error(final String message) {
        log("ERROR", message);
    }

    public void warn(final String message) {
        log("WARN", message);
    }

    public void debug(final String message) {
        log("DEBUG", message);
    }
}
