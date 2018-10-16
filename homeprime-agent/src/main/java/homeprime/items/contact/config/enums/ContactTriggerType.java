package homeprime.items.contact.config.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import homeprime.core.logger.IoTLogger;

/**
 * Types of contact sensor triggering.
 * 
 * @author Milan Ramljak
 * 
 */
public enum ContactTriggerType {

	/**
	 * Contact state changed from HIGH to LOW i.e. from ON to OFF
	 */
	HIGH_to_LOW,
	/**
	 * Contact state changed from LOW to HIGH i.e. from OFF to ON
	 */
	LOW_to_HIGH,
	/**
	 * Contact state changes.
	 */
	Any,
	/**
	 * A placeholder for invalid contact trigger type value.
	 */
	Unknown;

	@JsonCreator
	public static ContactTriggerType fromString(String contactTriggerType) {
		try {
			return valueOf(contactTriggerType);
		} catch (Exception e) {
			IoTLogger.getInstance().error("Contact trigger type: " + contactTriggerType + " is not supported, returning Unknown");
		}
		return ContactTriggerType.Unknown;
	}

}
