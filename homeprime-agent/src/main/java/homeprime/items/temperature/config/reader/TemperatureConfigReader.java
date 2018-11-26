package homeprime.items.temperature.config.reader;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.core.exceptions.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.core.utils.ThingUtils;
import homeprime.items.temperature.config.pojos.TemperatureSensors;

/**
 * Temperature settings loader.
 * 
 * @author Milan Ramljak
 * 
 */
public class TemperatureConfigReader {

	private static String tempSensors = null;

	/**
	 * Hidden constructor.
	 */
	private TemperatureConfigReader() {
	}

	public static TemperatureSensors getTemperatureSensors() throws ThingException {

		try {
			if (tempSensors == null) {
				tempSensors = ThingUtils
						.readFile(ThingProperties.getInstance().getThingConfigPath() + "temperature_sensors.json");
			}
			final ObjectMapper mapper = new ObjectMapper();
			final TemperatureSensors tempSensorsPojo = mapper.readValue(tempSensors, TemperatureSensors.class);
			return tempSensorsPojo;
		} catch (IOException e) {
			throw new ThingException(
					"ERROR ThingTemperatureConfigReader.getTemperatureSensors() Failed to parse temperature_sensors.json",
					e);
		}
	}
	
	/**
	 * Force that next getTemperatureSensors method call reads configuration again.
	 */
	public static void resyncConfig() {
		tempSensors = null;
	}

}
