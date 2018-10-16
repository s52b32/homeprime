package homeprime.items.relay.config.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import homeprime.core.logger.IoTLogger;

/**
 * Payload type of relay channel.
 * 
 * @author Milan Ramljak
 * 
 */
public enum PayloadType {

    Light, HVAC, Amplifier, LCD, WaterPump, WaterHeater, FloorHeater, WallHeater, Door, Generic, Unknown;

    @JsonCreator
    public static PayloadType fromString(String param) {
	try {
	    return valueOf(param);
	} catch (Exception e) {
	    IoTLogger.getInstance().error("Payload type: " + param + " is not supported, returning Unknown");
	}
	return PayloadType.Unknown;
    }

}
