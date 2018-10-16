package homeprime.system.config;

import homeprime.core.exceptions.ThingException;

/**
 * 
 * Main interface representing all info available of the thing system.
 * 
 * @author Milan Ramljak
 * 
 */
public interface ThingSystemInfo {

    public ThingHardwareInfo getThingHardwareInfo() throws ThingException;

    public ThingMemoryInfo getThingMemoryInfo() throws ThingException;

    public ThingOsInfo getThingOsInfo() throws ThingException;

}
