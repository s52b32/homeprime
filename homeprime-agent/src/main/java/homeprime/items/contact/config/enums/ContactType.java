package homeprime.items.contact.config.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import homeprime.core.logger.IoTLogger;

/**
 * Types of contact.
 * 
 * @author Milan Ramljak
 * 
 */
public enum ContactType {

	/**
	 * Contact device is wall switch.
	 */
	WallSwitch,
	/**
	 * Contact device is motion sensor.
	 */
	MotionSwitch,
	/**
	 * Contact device is magnetic sensor.
	 */
	MagneticSwitch,
	/**
	 * Contact device is any (generic) type of contact.
	 */
	Generic,
	/**
	 * A placeholder for invalid contact type value.
	 */
	Unknown;

	@JsonCreator
	public static ContactType fromString(String contactType) {
		try {
			return valueOf(contactType);
		} catch (Exception e) {
			IoTLogger.getInstance().error("Contact type: " + contactType + " is not supported, returning Unknown");
		}
		return ContactType.Unknown;
	}

}
