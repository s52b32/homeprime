package homeprime.system.raspberrypi;

import java.io.IOException;

import com.pi4j.system.SystemInfo;

import homeprime.core.exception.ThingException;
import homeprime.system.ThingHardwareInfo;

/**
 * Default implementation for thing hardware info data retrieval on Raspberry
 * PI.
 *
 * @author Milan Ramljak
 *
 */
public class ThingHardwareInfoImpl implements ThingHardwareInfo {

    @Override
    public String getSerialNumber() throws ThingException {
        try {
            return SystemInfo.getSerial();
        } catch (UnsupportedOperationException e) {
            throw new ThingException("Failed to get RaspberryPi hardware board serial number", e);
        } catch (IOException e) {
            throw new ThingException("Failed to get RaspberryPi hardware board serial number", e);
        } catch (InterruptedException e) {
            throw new ThingException("Failed to get RaspberryPi hardware board serial number", e);
        }
    }

    @Override
    public String getBoardType() throws ThingException {
        try {
            return SystemInfo.getBoardType().name();
        } catch (UnsupportedOperationException e) {
            throw new ThingException("Failed to get RaspberryPi hardware board type", e);
        } catch (IOException e) {
            throw new ThingException("Failed to get RaspberryPi hardware board type", e);
        } catch (InterruptedException e) {
            throw new ThingException("Failed to get RaspberryPi hardware board type", e);
        }
    }

    @Override
    public float getCpuTemperature() throws ThingException {
        try {
            return SystemInfo.getCpuTemperature();
        } catch (NumberFormatException e) {
            throw new ThingException("Failed to get RaspberryPi hardware CPU temperature", e);
        } catch (UnsupportedOperationException e) {
            throw new ThingException("Failed to get RaspberryPi hardware CPU temperature", e);
        } catch (IOException e) {
            throw new ThingException("Failed to get RaspberryPi hardware CPU temperature", e);
        } catch (InterruptedException e) {
            throw new ThingException("Failed to get RaspberryPi hardware CPU temperature", e);
        }
    }

}
