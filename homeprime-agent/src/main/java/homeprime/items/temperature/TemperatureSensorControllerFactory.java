package homeprime.items.temperature;

import homeprime.core.exceptions.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.items.temperature.raspberrypi.TemperatureSensorControllerImpl;

/**
 * Thing temperature sensor reader factory.
 * 
 * @author Milan Ramljak
 */
public class TemperatureSensorControllerFactory {

    /**
     * Hidden constructor.
     */
    private TemperatureSensorControllerFactory() {}

    public static TemperatureSensorController getTemperatureSensorReader() throws ThingException {
        switch (ThingProperties.getInstance().getThingSystemType()) {
        case RaspberryPi:
            return new TemperatureSensorControllerImpl();
        case BananaPi:
            throw new ThingException("Thing temperature sensor reader for BannanaPI note supported yet.");
        case BeagleBoneBlack:
            throw new ThingException("Thing temperature sensor reader for BeagleBoneBlack note supported yet.");
        case Mock:
            return new homeprime.items.temperature.mock.TemperatureSensorControllerImpl();
        default:
            throw new ThingException("Thing temperature senosr reader implementation for Unknown system type cannot be created.");
        }
    }

}
