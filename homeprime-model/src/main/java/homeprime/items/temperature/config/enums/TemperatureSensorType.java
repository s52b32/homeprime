package homeprime.items.temperature.config.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Supported temperature sensor types.
 * 
 * @author Milan Ramljak
 * 
 */
public enum TemperatureSensorType {

	LM75, LM75A, Unknown;

	@JsonCreator
	public static TemperatureSensorType fromString(String param) {
		try {
			return valueOf(param);
		} catch (Exception e) {
		}
		return TemperatureSensorType.Unknown;
	}

}
