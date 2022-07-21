package homeprime.system.raspberrypi;

import java.io.IOException;
import java.text.ParseException;

import com.pi4j.system.SystemInfo;

import homeprime.core.exception.ThingException;
import homeprime.system.ThingOsInfo;

/**
 * Default implementation for thing OS info data retrieval on Raspberry PI.
 * 
 * @author emilram
 * 
 */
public class ThingOSInfoImpl implements ThingOsInfo {

	@Override
	public String getOsName() throws ThingException {
		return SystemInfo.getOsName();
	}

	@Override
	public String getOsVersion() throws ThingException {
		return SystemInfo.getOsVersion();
	}

	@Override
	public String getOsArchitecture() throws ThingException {
		return SystemInfo.getOsArch();
	}

	@Override
	public String getOsFirmwareBuild() throws ThingException {
		try {
			return SystemInfo.getOsFirmwareBuild();
		} catch (UnsupportedOperationException e) {
			throw new ThingException("Failed to get OS firmware build", e);
		} catch (IOException e) {
			throw new ThingException("Failed to get OS firmware build", e);
		} catch (InterruptedException e) {
			throw new ThingException("Failed to get OS firmware build", e);
		}
	}

	@Override
	public String getOsFirmwareDate() throws ThingException {
		try {
			return SystemInfo.getOsFirmwareDate();
		} catch (ParseException e) {
			throw new ThingException("Failed to get OS firmware date", e);
		} catch (UnsupportedOperationException e) {
			throw new ThingException("Failed to get OS firmware date", e);
		} catch (IOException e) {
			throw new ThingException("Failed to get OS firmware date", e);
		} catch (InterruptedException e) {
			throw new ThingException("Failed to get OS firmware date", e);
		}
	}

}
