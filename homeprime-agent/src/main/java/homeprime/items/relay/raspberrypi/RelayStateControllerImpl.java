package homeprime.items.relay.raspberrypi;

import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.readers.items.relay.RelayConfigReader;
import homeprime.items.relay.RelayStateController;
import homeprime.items.relay.config.enums.RelayType;
import homeprime.items.relay.config.pojos.Relay;
import homeprime.items.relay.config.pojos.Relays;

/**
 * Manage relay channel pin state connected to RaspberryPi.
 *
 * @author Milan Ramljak
 */
public class RelayStateControllerImpl implements RelayStateController {

    @Override
    public Boolean readState(Relay relayData) throws ThingException {
        if (relayData == null) {
            throw new ThingException(
                    "ERROR RelayStateControllerImpl.readState() Cannot check relay state if Relay pojo is null");
        }
        final int relayPin = relayData.getPin();
        Boolean state = readDigitalGpioStatus(relayPin);
        IoTLogger.getInstance().info("Read state of relay: " + relayData.getName() + "("
                + relayData.getRelayType().toString() + ")" + " is " + state);
        final RelayType relayType = relayData.getRelayType();
        // NC relay has input pin state of FALSE meaning ON.
        // NO relay has input pin state of TRUE meaning ON.
        if (relayType == RelayType.NC) {
            IoTLogger.getInstance().info("Reversing relay state for NC type of relay.");
            state = !state;
        }
        IoTLogger.getInstance().info("Current state of relay: " + relayData.getName() + "("
                + relayData.getRelayType().toString() + ")" + " is " + state);
        return state;
    }

    @Override
    public void toggleState(Relay relayData) throws ThingException {
        final GpioPinDigitalOutput relay = getOrProvisionGpio(GpioFactory.getInstance(), relayData);
        relay.toggle();
        IoTLogger.getInstance().info("Toggling current state of relay: " + relayData.getName());
    }

    @Override
    public void setState(Relay relayData, boolean newState) throws ThingException {
        final GpioPinDigitalOutput relay = getOrProvisionGpio(GpioFactory.getInstance(), relayData);
        if (relayData.getRelayType() == RelayType.NC) {
            IoTLogger.getInstance().info("Reversing relay state for NC type of relay.");
            relay.setState(!newState);
        } else {
            relay.setState(newState);
        }
        IoTLogger.getInstance().info("State of relay: " + relayData.getName() + "("
                + relayData.getRelayType().toString() + ")" + " set to " + newState);
    }

    @Override
    public void initialize() throws ThingException {
        final Relays relays = RelayConfigReader.getRelays();
        if (relays != null) {
            List<Relay> relaies = relays.getRelays();
            if (!relaies.isEmpty()) {
                // Loop all relays within configuration
                for (Relay relay : relaies) {
                    // set relay in OFF state
                    setStateAsIs(relay, false);
                }
            }
        }
    }

    /**
     * Helper method to set relay state regardless of relay type.
     *
     * @param relayData relay object
     * @param newState  state to set
     */
    private void setStateAsIs(Relay relayData, boolean newState) {
        final GpioPinDigitalOutput relay = getOrProvisionGpio(GpioFactory.getInstance(), relayData);
        relay.setState(newState);
        IoTLogger.getInstance().info("State of relay: " + relayData.getName() + "("
                + relayData.getRelayType().toString() + ")" + " set to " + newState);
    }

    /**
     * Helper method for reading pin state of RaspberryPi pin.
     *
     * @param pinAddress - gpio port number
     * @return {@code true} if pin is set to ON otherwise {@code false}.
     *         {@code null} if error occurs.
     */
    private Boolean readDigitalGpioStatus(int pinAddress) {
        GpioFactory.getInstance();
        Gpio.pinMode(pinAddress, Gpio.OUTPUT);
        return Gpio.digitalRead(pinAddress) == 0;
    }

    /**
     * Helper method to check and return already provision GPIO pin or provision it and then return.
     *
     * @param gpio
     * @param relay
     * @return {@code GpioPinDigitalOutput} object of provisioned GPIO pin
     */
    public static GpioPinDigitalOutput getOrProvisionGpio(GpioController gpio, Relay relay) {
        if (gpio.getProvisionedPin(RaspiPin.getPinByAddress(relay.getPin())) != null) {
            return (GpioPinDigitalOutput) gpio.getProvisionedPin(RaspiPin.getPinByAddress(relay.getPin()));
        } else {
            return gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(relay.getPin()));
        }
    }

}
