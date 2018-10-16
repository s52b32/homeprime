package homeprime.system.mock;

import homeprime.system.config.ThingHardwareInfo;
import homeprime.system.config.ThingMemoryInfo;
import homeprime.system.config.ThingOsInfo;
import homeprime.system.config.ThingSystemInfo;

/**
 * Mocked implementation for thing system info data retrieval.
 * 
 * @author Milan Ramljak
 * 
 */
public class ThingSystemInfoImpl implements ThingSystemInfo {

    @Override
    public ThingHardwareInfo getThingHardwareInfo() {
	return new ThingHardwareInfoImpl();
    }

    @Override
    public ThingMemoryInfo getThingMemoryInfo() {
	return new ThingMemoryInfoImpl();
    }

    @Override
    public ThingOsInfo getThingOsInfo() {
	return new ThingOSInfoImpl();
    }

}
