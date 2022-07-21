package homeprime.system.bannanapi;

import homeprime.core.exception.ThingException;
import homeprime.system.ThingDiskInfo;
import homeprime.system.ThingHardwareInfo;
import homeprime.system.ThingMemoryInfo;
import homeprime.system.ThingOsInfo;
import homeprime.system.ThingSystemInfo;

/**
 * Default implementation for thing system info data retrieval on Bannana PI.
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

    @Override
    public ThingDiskInfo getThingDiskInfo() throws ThingException {
        return new ThingDiskInfoImpl();
    }

    @Override
    public Long getSystemUptime() throws ThingException {
        // TODO Auto-generated method stub
        return null;
    }

}
