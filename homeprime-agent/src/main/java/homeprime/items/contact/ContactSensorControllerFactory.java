package homeprime.items.contact;

import homeprime.core.exception.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.items.contact.raspberrypi.ContactSensorControllerImpl;

/**
 * Thing contact sensors state reader factory.
 * 
 * @author Milan Ramljak
 */
public class ContactSensorControllerFactory {

	/**
	 * Hidden constructor.
	 */
	private ContactSensorControllerFactory() {
	}

	public static ContactSensorController getContactSensorsReader() throws ThingException {
		switch (ThingProperties.getInstance().getThingSystemType()) {
		case RaspberryPi:
			return ContactSensorControllerImpl.getInstance();
		case BananaPi:
			throw new ThingException("Thing contact sensors state reader for BannanaPI not supported yet.");
		case BeagleBoneBlack:
			throw new ThingException("Thing  contact sensors state reader for BeagleBoneBlack not supported yet.");
		case Mock:
			return homeprime.items.contact.mock.ContactSensorControllerImpl.getInstance();
		default:
			throw new ThingException(
					"Thing  contact sensors state reader implementation for Unknown cannot be created.");
		}
	}

}
