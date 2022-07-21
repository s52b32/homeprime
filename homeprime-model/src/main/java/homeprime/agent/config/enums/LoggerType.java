package homeprime.agent.config.enums;

/**
 * Types of operating systems (OS) running homeprime agent.
 *
 * @author Milan Ramljak
 *
 */
public enum LoggerType {

    /**
     * Logger is file.
     */
    File,
    /**
     * Logger is standard console output.
     */
    Standard,
    /**
     * Logger does not log anything.
     */
    Void,
    /**
     * Placeholder for unknown logger type.
     */
    Unknown

}
