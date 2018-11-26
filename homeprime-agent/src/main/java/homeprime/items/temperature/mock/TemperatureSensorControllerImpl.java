package homeprime.items.temperature.mock;

import java.io.IOException;

import homeprime.core.exceptions.ThingException;
import homeprime.core.utils.ThingUtils;
import homeprime.items.temperature.TemperatureSensorController;
import homeprime.items.temperature.config.pojos.TemperatureSensor;

/**
 * Implementation of temperature sensor reader on mocked system.
 * 
 * @author Milan Ramljak
 */
public class TemperatureSensorControllerImpl implements TemperatureSensorController {

	@Override
	public float readTemperature(TemperatureSensor temperatureSensorData) throws ThingException {
		if (temperatureSensorData != null) {
			try {
				return Float.parseFloat(ThingUtils.readFile("mock/temperature.data"));
			} catch (NumberFormatException e) {
				return (float) -99.99;
			} catch (IOException e) {
				return (float) -99.99;
			}
		} else {
			// This represents that something went wrong.
			return (float) -99.99;
		}
	}

}
