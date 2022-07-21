package homeprime.system.bannanapi;

import homeprime.core.exception.ThingException;
import homeprime.system.ThingOsInfo;

/**
 * Default implementation for thing OS info data retrieval on Bannana PI.
 * 
 * @author Milan Ramljak
 * 
 */
public class ThingOSInfoImpl implements ThingOsInfo {

	@Override
	public String getOsName() throws ThingException {
		throw new ThingException("ERROR ThingOSInfoImpl.getOSName() Not implemented for BannanaPi");
	}

	@Override
	public String getOsVersion() throws ThingException {
		throw new ThingException("ERROR ThingOSInfoImpl.getOSVersion() Not implemented for BannanaPi");
	}

	@Override
	public String getOsArchitecture() throws ThingException {
		throw new ThingException("ERROR ThingOSInfoImpl.getOSArchitecture() Not implemented for BannanaPi");
	}

	@Override
	public String getOsFirmwareBuild() throws ThingException {
		throw new ThingException("ERROR ThingOSInfoImpl.getOSFirmwareBuild() Not implemented for BannanaPi");
	}

	@Override
	public String getOsFirmwareDate() throws ThingException {
		throw new ThingException("ERROR ThingOSInfoImpl.getOSFirmwareDate() Not implemented for BannanaPi");
	}

}
