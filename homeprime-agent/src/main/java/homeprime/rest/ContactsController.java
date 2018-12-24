package homeprime.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.exceptions.ThingException;
import homeprime.items.contact.ContactSensorControllerFactory;
import homeprime.items.contact.config.pojos.ContactSensor;
import homeprime.items.contact.config.pojos.ContactSensors;
import homeprime.items.contact.config.reader.ContactsConfigReader;

/**
 * Spring REST controller for thing contact sensor reader operations.
 * 
 * @author Milan Ramljak
 * 
 */
@RestController
public class ContactsController {

	@RequestMapping("/Thing/Contacts")
	public ResponseEntity<ContactSensors> getContactSensors() {
		try {
			return new ResponseEntity<ContactSensors>(ContactsConfigReader.getContactSensors(), HttpStatus.OK);
		} catch (ThingException e) {
			return new ResponseEntity<ContactSensors>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Contacts/sync")
	public ResponseEntity<String> syncContactConfig() {
		ContactsConfigReader.resyncConfig();
		return new ResponseEntity<String>("Contacts config re-sync scheduled", HttpStatus.OK);
	}

	@RequestMapping("/Thing/Contacts/overview")
	public ResponseEntity<ContactSensors> getContactSensorsOverview() throws ThingException {
		ContactSensors contactSensorsPojo = null;
		try {
			contactSensorsPojo = ContactsConfigReader.getContactSensors();
		} catch (ThingException e) {
			throw new ThingException("ERROR Cannot read contacts configuration.", e);
		}
		if (contactSensorsPojo != null) {
			final List<ContactSensor> contactSensors = contactSensorsPojo.getContacts();
			if (!contactSensors.isEmpty()) {
				for (ContactSensor contactSensor : contactSensors) {
					contactSensor.setState(ContactSensorControllerFactory.getContactSensorsReader().readContactState(contactSensor));
				}
			}
			return new ResponseEntity<ContactSensors>(contactSensorsPojo, HttpStatus.OK);
		}
		return null;
	}

	@RequestMapping("/Thing/Contacts/{contactSensorId}")
	public ResponseEntity<ContactSensor> getTemperatureSensorById(
			@PathVariable(value = "contactSensorId") int contactSensorId) {
		try {
			final ContactSensor contactSensor = findContactSensorById(contactSensorId);
			if (contactSensor != null) {
				return new ResponseEntity<ContactSensor>(contactSensor, HttpStatus.OK);
			} else {
				return new ResponseEntity<ContactSensor>(HttpStatus.NOT_FOUND);
			}
		} catch (ThingException e) {
			return new ResponseEntity<ContactSensor>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Contacts/{contactSensorId}/read")
	public ResponseEntity<Boolean> getContactSensorValue(@PathVariable(value = "contactSensorId") int contactSensorId) {
		try {
			final ContactSensor contactSensor = findContactSensorById(contactSensorId);
			if (contactSensor != null) {
				return new ResponseEntity<Boolean>(
						ContactSensorControllerFactory.getContactSensorsReader().readContactState(contactSensor),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
			}
		} catch (ThingException e) {
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}
	}

	private ContactSensor findContactSensorById(int contactSensorId) throws ThingException {
		ContactSensors contactSensorsPojo = null;
		try {
			contactSensorsPojo = ContactsConfigReader.getContactSensors();
		} catch (ThingException e) {
			throw new ThingException("ERROR ThingContactSensorController.findContactSensorById( " + contactSensorId
					+ ") Failed to get contact sensors.", e);
		}
		if (contactSensorsPojo != null) {
			final List<ContactSensor> contactSensors = contactSensorsPojo.getContacts();
			if (!contactSensors.isEmpty()) {
				for (ContactSensor contactSensor : contactSensors) {
					if (contactSensor.getId() == contactSensorId) {
						return contactSensor;
					}
				}
			}
		}
		return null;
	}
}
