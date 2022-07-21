package homeprime.core.model.readers.items.temperature;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.core.exception.ThingException;
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
    /**
     * File describing temperature sensor items.
     */
    public static final String TEMPERATURES_CONFIGURATION_FILE_NAME = "temperature_sensors.json";
    private static String tempSensors = null;

    /**
     * Hidden constructor.
     */
    private TemperatureConfigReader() {
    }

    public static TemperatureSensors getTemperatureSensors() throws ThingException {

        try {
            if (tempSensors == null) {
                tempSensors = ThingUtils.readFile(
                        ThingProperties.getInstance().getItemsRootPath() + TEMPERATURES_CONFIGURATION_FILE_NAME);
            }
            final ObjectMapper mapper = new ObjectMapper();
            final TemperatureSensors tempSensorsPojo = mapper.readValue(tempSensors, TemperatureSensors.class);
            return tempSensorsPojo;
        } catch (Exception e) {
            throw new ThingException("ERROR TemperatureConfigReader.getTemperatureSensors() Failed to parse "
                    + TEMPERATURES_CONFIGURATION_FILE_NAME, e);
        }
    }

    /**
     * Force that next getTemperatureSensors method call reads configuration again.
     */
    public static void reloadConfig() {
        tempSensors = null;
    }

}
