package homeprime.items.temperature.config.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import homeprime.core.logger.IoTLogger;

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
			IoTLogger.getInstance().error("Temperature value type: " + param + " is not supported, returning Unknown");
		}
		return TemperatureValueType.Unknown;
	}

}
