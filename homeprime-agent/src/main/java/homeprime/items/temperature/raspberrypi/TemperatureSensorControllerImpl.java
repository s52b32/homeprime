package homeprime.items.temperature.raspberrypi;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import homeprime.core.exception.ThingException;
import homeprime.items.temperature.TemperatureSensorController;
import homeprime.items.temperature.config.pojos.TemperatureSensor;

/**
 * Implementation of temperature sensor reader for Raspberry PI.
 *
 * @author Milan Ramljak
 *
 */
public class TemperatureSensorControllerImpl implements TemperatureSensorController {

    @Override
    public float readTemperature(TemperatureSensor temperatureSensorData) throws ThingException {
        if (temperatureSensorData != null) {
            return readI2CTemperature(temperatureSensorData.getI2cBus(), temperatureSensorData.getI2cAddress());
        } else {
            // This represents that something went wrong.
            return (float) -99.99;
        }
    }

    /**
     * Helper method to read temperature from sensor connected via I2C to Raspberyy PI.
     *
     * @param i2cBusNumber - I2C bus number.
     * @param i2cAddress   - I2C hex address of sensor.
     * @return temperature value
     * @throws ThingException - in case of issues to read I2C bus on provided address.
     */
    private float readI2CTemperature(Integer i2cBusNumber, String i2cAddress) throws ThingException {
        I2CBus i2cBus;
        try {
            i2cBus = I2CFactory.getInstance(i2cBusNumber);
            final Integer i2cDeviceAddress = Integer.parseInt(i2cAddress.replace("0x", ""), 16);
            I2CDevice tempSensor = i2cBus.getDevice(i2cDeviceAddress);
            byte[] buffer = new byte[2];
            tempSensor.read(0, buffer, 0, 2);

            // Clean object before return
            i2cBus.close();
            tempSensor = null;
            i2cBus = null;

            return convertLM75Temperature(buffer);

        } catch (UnsupportedBusNumberException e) {
            throw new ThingException("ERROR TemperatureSensorReaderImpl.readI2CTemperature(" + i2cBusNumber + ", "
                    + i2cAddress + ") Unsupported I2C bus selected.", e);
        } catch (IOException e) {
            throw new ThingException("ERROR TemperatureSensorReaderImpl.readI2CTemperature(" + i2cBusNumber + ", "
                    + i2cAddress + ") Failed to read from I2C device.", e);
        }
    }

    /**
     * Helper method to convert I2C LM75 reading of temperature to decimal number.
     *
     * @param sensorBuffer two byte buffer read from sensor
     * @return temperature as decimal number
     */
    private float convertLM75Temperature(byte[] sensorBuffer) {
        float temperature = (float) 0.0;
        // if MSB is higher than 127 it means that sensor value represents
        // nagative temperature
        if (sensorBuffer[0] < 127) {
            temperature = sensorBuffer[0];
        } else {
            temperature = sensorBuffer[0] - 255;
        }

        // if LSB is higher than 127 it means that sensor read that value
        // has 0.5
        if (sensorBuffer[1] > 127) {
            if (temperature < 0) {
                temperature = (float) (temperature - 0.5);
            } else {
                temperature = (float) (temperature + 0.5);
            }
        }
        return temperature;
    }

}
