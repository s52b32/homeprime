package homeprime.system;

import homeprime.core.exception.ThingException;

/**
 *
 * Main interface representing all info available of the thing system.
 *
 * @author Milan Ramljak
 *
 */
public interface ThingSystemInfo {

    ThingHardwareInfo getThingHardwareInfo() throws ThingException;

    ThingMemoryInfo getThingMemoryInfo() throws ThingException;

    ThingDiskInfo getThingDiskInfo() throws ThingException;

    ThingOsInfo getThingOsInfo() throws ThingException;

    Long getSystemUptime() throws ThingException;

}
