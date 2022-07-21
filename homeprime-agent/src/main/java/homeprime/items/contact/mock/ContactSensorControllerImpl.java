package homeprime.items.contact.mock;

import java.util.Random;

import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.items.contact.ContactSensorController;
import homeprime.items.contact.config.pojos.ContactSensor;

/**
 * Implementation of contact sensor state control on mocked system.
 * 
 * @author Milan Ramljak
 * 
 */
public class ContactSensorControllerImpl implements ContactSensorController {

	private static ContactSensorControllerImpl singleton = null;

	@Override
	public Boolean readContactState(ContactSensor contactSensorData) throws ThingException {
		if (contactSensorData == null) {
			throw new ThingException(
					"ERROR ContactSensorsReaderImpl.readContactState() Cannot check contact state if ContactSensor pojo is null");
		}
		// Generate random boolean value to represent contact status.
		Boolean state = new Random().nextBoolean();
		IoTLogger.getInstance().info("Current state of contact : " + contactSensorData.getName() + " is " + state);
		return state;
	}

	public static ContactSensorControllerImpl getInstance() throws ThingException {
		if (singleton == null) {
			singleton = new ContactSensorControllerImpl();
		}
		return singleton;
	}

}
