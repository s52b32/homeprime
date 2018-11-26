package homeprime.system.config;

import homeprime.core.exceptions.ThingException;

/**
 * 
 * Interface describing thing hardware information.
 * 
 * @author Milan Ramljak
 * 
 */
public interface ThingHardwareInfo {

	/**
	 * Get serial number of the thing hardware.
	 * 
	 * @return serial number
	 * @throws ThingException in case of failure in retrieval procedure
	 */
	public String getSerialNumber() throws ThingException;

	/**
	 * Get board type of the thing hardware.
	 * 
	 * @return thing board type
	 * @throws ThingException in case of failure in retrieval procedure
	 */
	public String getBoardType() throws ThingException;

	/**
	 * Get CPU temperature of the thing hardware.
	 * 
	 * @return thing CPU temperature
	 * @throws ThingException in case of failure in retrieval procedure
	 */
	public float getCpuTemperature() throws ThingException;

}
