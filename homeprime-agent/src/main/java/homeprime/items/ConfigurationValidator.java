package homeprime.items;

import homeprime.core.exceptions.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.items.contact.config.reader.ContactsConfigReader;
import homeprime.items.relay.config.reader.RelayConfigReader;
import homeprime.items.temperature.config.reader.TemperatureConfigReader;

/**
 * Validate configuration files.
 * 
 * @author Milan Ramljak
 *
 */
public class ConfigurationValidator {

	/**
	 * Hidden constructor.
	 */
	private ConfigurationValidator() {
	}

	/**
	 * Helper method to validate JSON configuration data.
	 * 
	 * @return {@code true} if successful, otherwise {@code false}
	 */
	public static boolean validate() {
		boolean validationResult = true;
		try {
			ContactsConfigReader.getContactSensors();
		} catch (ThingException e) {
			IoTLogger.getInstance().error("Provided contacts.json is not valid!");
			validationResult = false;
		}
		try {
			RelayConfigReader.getRelays();
		} catch (ThingException e) {
			IoTLogger.getInstance().error("Provided relays.json is not valid!");
			validationResult = false;
		}
		try {
			TemperatureConfigReader.getTemperatureSensors();
		} catch (ThingException e) {
			IoTLogger.getInstance().error("Provided temperatures.json is not valid!");
			validationResult = false;
		}
		return validationResult;

	}

}
