package homeprime.items.relay.config.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import homeprime.core.logger.IoTLogger;

/**
 * Type of relay channel.
 * 
 * @author Milan Ramljak
 * 
 */
public enum RelayType {

    /**
     * Normally closed.
     */
    NC,
    /**
     * Normally opened.
     */
    NO,
    /**
     * Information not provided.
     */
    Unknown;

    @JsonCreator
    public static RelayType fromString(String param) {
	try {
	    return valueOf(param);
	} catch (Exception e) {
	    IoTLogger.getInstance().error("Relay type: " + param + " is not supported, returning Unknown");
	}
	return RelayType.Unknown;
    }

}
