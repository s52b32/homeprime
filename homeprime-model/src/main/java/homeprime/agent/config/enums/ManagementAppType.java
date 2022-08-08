package homeprime.agent.config.enums;

/**
 * Types of supported applications that can control agent.
 *
 * @author Milan Ramljak
 *
 */
public enum ManagementAppType {
    /**
     * Open HAB as remote consumer entity.
     */
    OpenHAB,
    /**
     * MQTT message Bus.
     */
    MQTT,
    /**
     * Agent acts as standalone system.
     */
    Standalone,
    /**
     * In case of used is not known.
     */
    Unknown;

    @Override
    public String toString() {
        return super.toString();
    }

}
