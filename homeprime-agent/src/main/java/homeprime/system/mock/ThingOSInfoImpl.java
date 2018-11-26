package homeprime.system.mock;

import homeprime.core.exceptions.ThingException;
import homeprime.system.config.ThingOsInfo;

/**
 * Mocked implementation for thing OS info data retrieval.
 * 
 * @author Milan Ramljak
 */
public class ThingOSInfoImpl implements ThingOsInfo {

	@Override
	public String getOsName() throws ThingException {
		return "Linux";
	}

	@Override
	public String getOsVersion() throws ThingException {
		return "1.0";
	}

	@Override
	public String getOsArchitecture() throws ThingException {
		return "x64";
	}

	@Override
	public String getOsFirmwareBuild() throws ThingException {
		return "FBMOCK";
	}

	@Override
	public String getOsFirmwareDate() throws ThingException {
		return "12011999";
	}

}
