package homeprime.system.raspberrypi;

import homeprime.core.exceptions.ThingException;
import homeprime.system.config.ThingHardwareInfo;
import homeprime.system.config.ThingMemoryInfo;
import homeprime.system.config.ThingOsInfo;
import homeprime.system.config.ThingSystemInfo;

/**
 * Default implementation for thing system info data retrieval on Raspberry PI.
 * 
 * @author Milan Ramljak
 * 
 */
public class ThingSystemInfoImpl implements ThingSystemInfo {

    @Override
    public ThingHardwareInfo getThingHardwareInfo() throws ThingException {
	return new ThingHardwareInfoImpl();
    }

    @Override
    public ThingMemoryInfo getThingMemoryInfo() throws ThingException {
	return new ThingMemoryInfoImpl();
    }

    @Override
    public ThingOsInfo getThingOsInfo() throws ThingException {
	return new ThingOSInfoImpl();
    }

}
