package homeprime.system.mock;

import java.util.Random;

import homeprime.core.exceptions.ThingException;
import homeprime.system.config.ThingHardwareInfo;

/**
 * Mocked implementation for thing hardware info data retrieval.
 * 
 * @author Milan Ramljak
 */
public class ThingHardwareInfoImpl implements ThingHardwareInfo {

    @Override
    public String getSerialNumber() throws ThingException {
        return "A1MOCK";
    }

    @Override
    public String getBoardType() throws ThingException {
        return "MOCKTYPE";
    }

    @Override
    public float getCpuTemperature() throws ThingException {
        return new Random().nextFloat();
    }

}
