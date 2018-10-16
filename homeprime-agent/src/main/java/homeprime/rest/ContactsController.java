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

    @RequestMapping("/Thing/Contacts/Synch")
    public ResponseEntity<String> syncContactConfig() {
	ContactsConfigReader.resyncConfig();
	return new ResponseEntity<String>("Contacts config re-sync scheduled", HttpStatus.OK);
    }

    @RequestMapping("/Thing/Contacts/Sewer")
    public ResponseEntity<ContactSensors> getSewerContactSensors() {
	try {
	    return new ResponseEntity<ContactSensors>(ContactsConfigReader.getSewer(), HttpStatus.OK);
	} catch (ThingException e) {
	    return new ResponseEntity<ContactSensors>(HttpStatus.BAD_REQUEST);
	}
    }

    @RequestMapping("/Thing/Contacts/Sewer/read")
    public ResponseEntity<String> getSewerSensorValue() {
	try {
	    final Integer sewerPercentage = getSewerPercentage();
	    if (sewerPercentage != null) {
		return new ResponseEntity<String>(sewerPercentage.toString(), HttpStatus.OK);
	    } else {
		return new ResponseEntity<String>("Cannot read sewer state if no confiuration found", HttpStatus.NOT_FOUND);
	    }
	} catch (ThingException e) {
	    return new ResponseEntity<String>("Failed to read sewer status", HttpStatus.BAD_REQUEST);
	}
    }

    @RequestMapping("/Thing/Contact/{contactSensorId}")
    public ResponseEntity<ContactSensor> getTemperatureSensorById(@PathVariable(value = "contactSensorId") int contactSensorId) {
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

    @RequestMapping("/Thing/Contact/{contactSensorId}/read")
    public ResponseEntity<Boolean> getContactSensorValue(@PathVariable(value = "contactSensorId") int contactSensorId) {
	try {
	    final ContactSensor contactSensor = findContactSensorById(contactSensorId);
	    if (contactSensor != null) {
		return new ResponseEntity<Boolean>(ContactSensorControllerFactory.getContactSensorsReader().readContactState(contactSensor),
			HttpStatus.OK);
	    } else {
		return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
	    }
	} catch (ThingException e) {
	    return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
	}
    }

    private Integer getSewerPercentage() throws ThingException {
	final ContactSensors contactSensors = ContactsConfigReader.getSewer();
	Integer sewerPercentage = null;
	if (contactSensors != null) {
	    final List<ContactSensor> contacts = contactSensors.getContacts();
	    if (!contacts.isEmpty()) {
		int countSewerContactsInStateTrue = 0;
		for (ContactSensor contactSensor : contacts) {
		    Boolean readContactState = ContactSensorControllerFactory.getContactSensorsReader().readContactState(contactSensor);
		    // If initial state id inverse we need to reverse contact
		    // current state
		    if (contactSensor.getInitialState() && readContactState != null) {
			readContactState = !readContactState;
		    }
		    if (readContactState) {
			countSewerContactsInStateTrue++;
		    }
		}
		final int sewerSize = contacts.size();
		sewerPercentage = countSewerContactsInStateTrue / sewerSize;
	    } else {
		throw new ThingException("Cannot calculate sewer percentage if contact list is empty");
	    }
	} else {
	    throw new ThingException("Cannot calculate sewer percentage if contact list doesn't exists");
	}
	return sewerPercentage;
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
