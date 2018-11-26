package homeprime.items.contact.config.reader;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.core.exceptions.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.core.utils.ThingUtils;
import homeprime.items.contact.config.pojos.ContactSensors;

/**
 * Reader of contacts.json configuration file into JSON POJO object.
 * 
 * @author Milan Ramljak
 * 
 */
public class ContactsConfigReader {

	private static String contactSensors = null;

	/**
	 * Hidden constructor.
	 */
	private ContactsConfigReader() {
	}

	public static ContactSensors getContactSensors() throws ThingException {
		try {
			if (contactSensors == null) {
				contactSensors = ThingUtils
						.readFile(ThingProperties.getInstance().getThingConfigPath() + "contacts.json");
			}
			final ObjectMapper mapper = new ObjectMapper();
			final ContactSensors tempContactSensorsPojo = mapper.readValue(contactSensors, ContactSensors.class);
			return tempContactSensorsPojo;
		} catch (IOException e) {
			throw new ThingException("ERROR ContactsConfigReader.getContactSensors() Failed to parse contacts.json", e);
		}
	}

	/**
	 * Force that next getContactSensors method call reads configuration again.
	 */
	public static void resyncConfig() {
		contactSensors = null;
	}

}
