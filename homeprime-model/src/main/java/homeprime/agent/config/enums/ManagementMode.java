package homeprime.agent.config.enums;

/**
 * Types of management operation modes.
 *
 * @author Milan Ramljak
 *
 */
public enum ManagementMode {

    /**
     * Agent is operated by client performing queries.
     */
    Pull,
    /**
     * Agent is pushing state changes towards client/management.
     */
    Push,
    /**
     * Placeholder for unknown management mode.
     */
    Unknown

}
