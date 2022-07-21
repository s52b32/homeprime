package homeprime.system;

import homeprime.core.exception.ThingException;

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
    String getSerialNumber() throws ThingException;

    /**
     * Get board type of the thing hardware.
     *
     * @return thing board type
     * @throws ThingException in case of failure in retrieval procedure
     */
    String getBoardType() throws ThingException;

    /**
     * Get CPU temperature of the thing hardware.
     *
     * @return thing CPU temperature
     * @throws ThingException in case of failure in retrieval procedure
     */
    float getCpuTemperature() throws ThingException;

}
