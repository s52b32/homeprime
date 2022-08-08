package homeprime.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.readers.items.contact.ContactsConfigReader;
import homeprime.items.contact.ContactSensorControllerFactory;
import homeprime.items.contact.config.pojos.ContactSensor;
import homeprime.items.contact.config.pojos.ContactSensors;

/**
 * Spring REST controller for thing contact sensor reader operations.
 *
 * @author Milan Ramljak
 *
 */
@RestController
public class ContactsController {

    @RequestMapping("/Thing/Contacts/reload-config")
    public ResponseEntity<String> syncContactConfig() {
        ContactsConfigReader.reloadConfig();
        return new ResponseEntity<String>("Contacts config re-sync scheduled", HttpStatus.OK);
    }

    @RequestMapping("/Thing/Contacts")
    public ResponseEntity<ContactSensors> getContactSensorsOverview() throws ThingException {
        ContactSensors contactSensorsPojo = new ContactSensors();
        return new ResponseEntity<ContactSensors>(contactSensorsPojo, HttpStatus.OK);
        // try {
        // contactSensorsPojo = ContactsConfigReader.getContactSensors();
        // } catch (ThingException e) {
        // throw new ThingException("ERROR Cannot read contacts configuration.", e);
        // }
        // if (contactSensorsPojo != null) {
        // final List<ContactSensor> contactSensors = contactSensorsPojo.getContacts();
        // if (!contactSensors.isEmpty()) {
        // for (ContactSensor contactSensor : contactSensors) {
        // contactSensor.setState(
        // ContactSensorControllerFactory.getContactSensorsReader().readContactState(contactSensor));
        // }
        // }
        // return new ResponseEntity<ContactSensors>(contactSensorsPojo, HttpStatus.OK);
        // }
        // return null;
    }

    // TODO: remove and sync in binding
    @RequestMapping("/Thing/Contacts/overview")
    public ResponseEntity<ContactSensors> getContactSensorsOverviewLegacy() throws ThingException {
        ContactSensors contactSensorsPojo = new ContactSensors();
        return new ResponseEntity<ContactSensors>(contactSensorsPojo, HttpStatus.OK);
        // try {
        // contactSensorsPojo = ContactsConfigReader.getContactSensors();
        // } catch (ThingException e) {
        // throw new ThingException("ERROR Cannot read contacts configuration.", e);
        // }
        // if (contactSensorsPojo != null) {
        // final List<ContactSensor> contactSensors = contactSensorsPojo.getContacts();
        // if (!contactSensors.isEmpty()) {
        // for (ContactSensor contactSensor : contactSensors) {
        // contactSensor.setState(
        // ContactSensorControllerFactory.getContactSensorsReader().readContactState(contactSensor));
        // }
        // }
        // return new ResponseEntity<ContactSensors>(contactSensorsPojo, HttpStatus.OK);
        // }
        // return null;
    }

    @RequestMapping("/Thing/Contacts/{contactSensorId}")
    public ResponseEntity<?> getContactSensorById(@PathVariable(value = "contactSensorId") int contactSensorId) {
        try {
            final ContactSensor contactSensor = findContactSensorById(contactSensorId);
            if (contactSensor != null) {
                contactSensor.setState(
                        ContactSensorControllerFactory.getContactSensorsReader().readContactState(contactSensor));
                return new ResponseEntity<ContactSensor>(contactSensor, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>(
                        "Contact (" + contactSensorId + ") not found in running configuration!", HttpStatus.NOT_FOUND);
            }
        } catch (ThingException e) {
            return new ResponseEntity<String>("Exception happen while reading contact state!", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/Thing/Contacts/{contactSensorId}/read")
    public ResponseEntity<String> getContactSensorReadById(
            @PathVariable(value = "contactSensorId") int contactSensorId) {
        try {
            final ContactSensor contactSensor = findContactSensorById(contactSensorId);
            if (contactSensor != null) {
                IoTLogger.getInstance().info("Get contact state by id");
                // success
                return new ResponseEntity<String>(getContactSensorState(
                        ContactSensorControllerFactory.getContactSensorsReader().readContactState(contactSensor)),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<String>(
                        "Contact (" + contactSensorId + ") not found in running configuration!", HttpStatus.NOT_FOUND);
            }
        } catch (ThingException e) {
            return new ResponseEntity<String>("Exception happen while reading contact state!", HttpStatus.BAD_REQUEST);
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

    private String getContactSensorState(boolean state) {
        if (state) {
            return "CLOSED";
        }
        return "OPEN";
    }
}
