package homeprime.items.contact.raspberrypi;

import java.util.List;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.wiringpi.Gpio;

import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.readers.items.contact.ContactsConfigReader;
import homeprime.items.contact.ContactSensorController;
import homeprime.items.contact.config.enums.ContactConnectionType;
import homeprime.items.contact.config.pojos.ContactSensor;
import homeprime.items.contact.config.pojos.ContactSensors;

/**
 * Implementation of contact sensor state control for Raspberry PI.
 *
 * @author Milan Ramljak
 *
 */
public class ContactSensorControllerImpl implements ContactSensorController {

    private static ContactSensorControllerImpl singleton = null;

    /**
     * Default constructor.
     */
    public ContactSensorControllerImpl() {
        System.out.println("I am HERE");
        // configure contacts to defined pull resistance
        configureContactsPullResistance();
    }

    @Override
    public Boolean readContactState(ContactSensor contactSensorData) throws ThingException {
        if (contactSensorData == null) {
            throw new ThingException(
                    "ERROR ContactSensorsReaderImpl.readContactState() Cannot check contact state if ContactSensor pojo is null");
        }
        final int contactPin = contactSensorData.getPin();
        final Boolean state = readDigitalGpioStatus(contactPin);
        IoTLogger.getInstance().info("Current state of contact : " + contactSensorData.getName() + " is " + state);
        return state;
    }

    public static ContactSensorControllerImpl getInstance() throws ThingException {
        if (singleton == null) {
            singleton = new ContactSensorControllerImpl();
        }
        return singleton;
    }

    /**
     * Helper method for reading pin state of RaspberryPi pin.
     *
     * @param pinAddress - gpio port number
     * @return {@code true} if pin is set to ON otherwise {@code false}.
     *         {@code null} if error occurs.
     */
    private Boolean readDigitalGpioStatus(int pinAddress) {
        GpioFactory.getInstance();
        Gpio.pinMode(pinAddress, Gpio.INPUT);
        Gpio.pullUpDnControl(pinAddress, Gpio.PUD_DOWN);
        return Gpio.digitalRead(pinAddress) == 1;
    }

    /**
     * Helper method to loop over all defined contacts and set their pull resistance to values defined in configuration
     * file.
     */
    private void configureContactsPullResistance() {
        // Configure contact wiring pull resistance for connected contacts
        ContactSensors contactSensorsPojo = null;
        try {
            contactSensorsPojo = ContactsConfigReader.getContactSensors();
        } catch (ThingException e) {
            IoTLogger.getInstance().error("configureContactsPullResistance: Cannot read contacts configuration.");
        }
        if (contactSensorsPojo != null) {
            final List<ContactSensor> contactSensors = contactSensorsPojo.getContacts();
            // check if there are defined contacts
            if (!contactSensors.isEmpty()) {
                // loop over each contract object
                for (ContactSensor contactSensor : contactSensors) {
                    // do this only if contact is actually connected (wired) to system
                    if (contactSensor.getStatus() == ContactConnectionType.Connected) {
                        GpioFactory.getInstance();
                        Gpio.pinMode(contactSensor.getPin(), Gpio.INPUT);
                        switch (contactSensor.getWiringType()) {
                            case PullDown:
                                Gpio.pullUpDnControl(contactSensor.getPin(), Gpio.PUD_DOWN);
                                break;
                            case PullUp:
                                Gpio.pullUpDnControl(contactSensor.getPin(), Gpio.PUD_UP);
                                break;
                            default:
                                Gpio.pullUpDnControl(contactSensor.getPin(), Gpio.PUD_OFF);
                                break;
                        }
                    }
                }
            }
        }
    }

}
