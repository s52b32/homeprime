package homeprime.system.beagleboneblack;

import homeprime.system.config.ThingHardwareInfo;
import homeprime.system.config.ThingMemoryInfo;
import homeprime.system.config.ThingOsInfo;
import homeprime.system.config.ThingSystemInfo;

/**
 * Default implementation for thing system info data retrieval on
 * BeagleBoneBlack.
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
