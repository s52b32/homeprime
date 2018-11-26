package homeprime.items.temperature;

import homeprime.core.exceptions.ThingException;
import homeprime.items.temperature.config.pojos.TemperatureSensor;

/**
 * Interface for temperature sensor reading.
 * 
 * @author Milan Ramljak
 * 
 */
public interface TemperatureSensorController {

	/**
	 * Read temperature from provided sensor.
	 * 
	 * @param temperatureSensorData temperature sensor data
	 * @return temperature from sensor
	 */
	public float readTemperature(TemperatureSensor temperatureSensorData) throws ThingException;

}
