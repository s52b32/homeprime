package homeprime.system.beagleboneblack;

import homeprime.core.exceptions.ThingException;
import homeprime.system.config.ThingOsInfo;

/**
 * Default implementation for thing OS info data retrieval on BeagleBoneBlack.
 * 
 * @author emilram
 * 
 */
public class ThingOSInfoImpl implements ThingOsInfo {

	@Override
	public String getOsName() throws ThingException {
		throw new ThingException("ERROR ThingOSInfoImpl.getOSName() Not implemented for BeagleBoneBlack");
	}

	@Override
	public String getOsVersion() throws ThingException {
		throw new ThingException("ERROR ThingOSInfoImpl.getOSVersion() Not implemented for BeagleBoneBlack");
	}

	@Override
	public String getOsArchitecture() throws ThingException {
		throw new ThingException("ERROR ThingOSInfoImpl.getOSArchitecture() Not implemented for BeagleBoneBlack");
	}

	@Override
	public String getOsFirmwareBuild() throws ThingException {
		throw new ThingException("ERROR ThingOSInfoImpl.getOSFirmwareBuild() Not implemented for BeagleBoneBlack");
	}

	@Override
	public String getOsFirmwareDate() throws ThingException {
		throw new ThingException("ERROR ThingOSInfoImpl.getOSFirmwareDate() Not implemented for BeagleBoneBlack");
	}

}
