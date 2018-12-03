package homeprime.items.relay.config.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Payload type of relay channel.
 * 
 * @author Milan Ramljak
 * 
 */
public enum PayloadType {

	Light, HVAC, Generic, Unknown;

	@JsonCreator
	public static PayloadType fromString(String param) {
		try {
			return valueOf(param);
		} catch (Exception e) {
		}
		return PayloadType.Unknown;
	}

}
