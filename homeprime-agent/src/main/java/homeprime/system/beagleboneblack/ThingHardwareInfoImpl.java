package homeprime.system.beagleboneblack;

import homeprime.core.exceptions.ThingException;
import homeprime.system.config.ThingHardwareInfo;

/**
 * Default implementation for thing hardware info data retrieval on
 * BeagleBoneBlack.
 * 
 * @author Milan Ramljak
 * 
 */
public class ThingHardwareInfoImpl implements ThingHardwareInfo {

    @Override
    public String getSerialNumber() throws ThingException {
	throw new ThingException("ERROR ThingHardwareInfoImpl.getSerialNumber() Not implemented for BeagleBoneBlack");
    }

    @Override
    public String getBoardType() throws ThingException {
	throw new ThingException("ERROR ThingHardwareInfoImpl.getBoardType() Not implemented for BeagleBoneBlack");
    }

    @Override
    public float getCpuTemperature() throws ThingException {
	throw new ThingException("ERROR ThingHardwareInfoImpl.getCPUTemperature() Not implemented for BeagleBoneBlack");
    }

}
