package homeprime.items.relay.raspberrypi;

import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

import homeprime.core.exceptions.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.items.relay.RelayStateController;
import homeprime.items.relay.config.enums.RelayType;
import homeprime.items.relay.config.pojos.Relay;
import homeprime.items.relay.config.pojos.Relays;
import homeprime.items.relay.config.reader.RelayConfigReader;

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

		final RelayType relayType = relayData.getRelayType();
		if (relayType == RelayType.NC) {
			IoTLogger.getInstance().info("Reversing relay state for NC type of relay.");
			state = !state;
		}
		IoTLogger.getInstance().info("Current state of relay: " + relayData.getName() + " is " + state);
		return state;
	}

	@Override
	public void toggleState(Relay relayData) throws ThingException {
		final int relayPin = relayData.getPin();
		GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalOutput relay = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(relayPin),
				relayData.getName());
		relay.toggle();
		gpio.shutdown();
		gpio.unprovisionPin(relay);
		IoTLogger.getInstance().info("Toggling current state of relay: " + relayData.getName());
	}

	@Override
	public void setState(Relay relayData, boolean newState) throws ThingException {
		final int relayPin = relayData.getPin();
		GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalOutput relay = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(relayPin),
				relayData.getName());
		relay.setState(newState);
		gpio.shutdown();
		gpio.unprovisionPin(relay);
		IoTLogger.getInstance().info("State of relay: " + relayData.getName() + " set to " + newState);
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

	@Override
	public void initialize() throws ThingException {
		final Relays relays = RelayConfigReader.getRelays();
		if (relays != null) {
			List<Relay> relaies = relays.getRelays();
			if (!relaies.isEmpty()) {
				// Loop all relay within configuration
				for (Relay relay : relaies) {
					// set relay in OFF state
					if (relay.getRelayType() == RelayType.NO) {
						setState(relay, true);
					} else {
						setState(relay, false);
					}
				}
			}
		}

	}

}
