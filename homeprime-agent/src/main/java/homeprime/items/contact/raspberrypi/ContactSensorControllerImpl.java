package homeprime.items.contact.raspberrypi;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.wiringpi.Gpio;

import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.items.contact.ContactSensorController;
import homeprime.items.contact.config.pojos.ContactSensor;

/**
 * Implementation of contact sensor state control for Raspberry PI.
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
		final int contactPin = contactSensorData.getPin();
		final Boolean state = readDigitalGpioStatus(contactPin);
		IoTLogger.getInstance().info("Current state of contact : " + contactSensorData.getName() + " is " + state);
		return state;
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

	public static ContactSensorControllerImpl getInstance() throws ThingException {
		if (singleton == null) {
			singleton = new ContactSensorControllerImpl();
		}
		return singleton;
	}

}
