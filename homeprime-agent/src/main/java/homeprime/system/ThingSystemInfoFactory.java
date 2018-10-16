package homeprime.system;

import homeprime.core.exceptions.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.system.config.ThingSystemInfo;
import homeprime.system.raspberrypi.ThingSystemInfoImpl;

/**
 * Thing system info factory.
 * 
 * @author Milan Ramljak
 * 
 */
public class ThingSystemInfoFactory {

    /**
     * Hidden constructor.
     */
    private ThingSystemInfoFactory() {
    }

    public static ThingSystemInfo getThingSystemInfo() throws ThingException {
	switch (ThingProperties.getInstance().getThingSystemType()) {
	case RaspberryPi:
	    return new ThingSystemInfoImpl();
	case BananaPi:
	    return new homeprime.system.bannanapi.ThingSystemInfoImpl();
	case BeagleBoneBlack:
	    return new homeprime.system.beagleboneblack.ThingSystemInfoImpl();
	case Mock:
        return new homeprime.system.mock.ThingSystemInfoImpl();
	default:
	    throw new ThingException("Thing system info implementation for Unknown icannot be created.");
	}
    }

}
