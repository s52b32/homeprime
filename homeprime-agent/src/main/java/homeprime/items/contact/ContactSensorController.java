package homeprime.items.contact;

import homeprime.core.exception.ThingException;
import homeprime.items.contact.config.pojos.ContactSensor;

/**
 * Interface for contact state reading.
 * 
 * @author Milan Ramljak
 * 
 */
public interface ContactSensorController {

	/**
	 * Get the state of contact checking state on specific pin.
	 * 
	 * @param contact contact sensor data pojo
	 * @return {@code true} if contact pin has HIGH state otherwise {@code false}
	 * @throws ThingException in case of error while reading contact state
	 */
	Boolean readContactState(ContactSensor contact) throws ThingException;

}
