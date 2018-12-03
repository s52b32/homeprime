package homeprime.items.temperature.config.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Supported value types for temperature sensors.
 * 
 * @author Milan Ramljak
 * 
 */
public enum TemperatureValueType {

	Celsius, Farenheit, Unknown;

	@JsonCreator
	public static TemperatureValueType fromString(String param) {
		try {
			return valueOf(param);
		} catch (Exception e) {
		}
		return TemperatureValueType.Unknown;
	}

}
