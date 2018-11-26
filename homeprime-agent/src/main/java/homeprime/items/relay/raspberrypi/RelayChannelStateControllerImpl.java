package homeprime.items.relay.raspberrypi;

import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

import homeprime.core.exceptions.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.items.relay.RelayChannelStateController;
import homeprime.items.relay.config.enums.RelayType;
import homeprime.items.relay.config.pojos.RelayBoard;
import homeprime.items.relay.config.pojos.RelayBoards;
import homeprime.items.relay.config.pojos.RelayChannel;
import homeprime.items.relay.config.reader.RelayConfigReader;

/**
 * Manage relay channel pin state connected to RaspberryPi.
 * 
 * @author Milan Ramljak
 */
public class RelayChannelStateControllerImpl implements RelayChannelStateController {

	@Override
	public Boolean readChannelState(RelayChannel relayChannelData) throws ThingException {
		if (relayChannelData == null) {
			throw new ThingException(
					"ERROR RelayChannelStateControllerImpl.readChannelState() Cannot check relay channel state if RelayChannel pojo is null");
		}
		final int relayChannelPin = relayChannelData.getPin();
		Boolean state = readDigitalGpioStatus(relayChannelPin);

		final RelayType relayType = relayChannelData.getRelayType();
		if (relayType == RelayType.NC) {
			IoTLogger.getInstance().info("Reversing relay channel state for NC type of channel.");
			state = !state;
		}
		IoTLogger.getInstance().info("Current state of relay channel: " + relayChannelData.getName() + " is " + state);
		return state;
	}

	@Override
	public void toggleChannelState(RelayChannel relayChannelData) throws ThingException {
		final int relayChannelPin = relayChannelData.getPin();
		GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalOutput relayChannel = gpio
				.provisionDigitalOutputPin(RaspiPin.getPinByAddress(relayChannelPin), relayChannelData.getName());
		relayChannel.toggle();
		gpio.shutdown();
		gpio.unprovisionPin(relayChannel);
		IoTLogger.getInstance().info("Toggling current state of relay channel: " + relayChannelData.getName());
	}

	@Override
	public void setChannelState(RelayChannel relayChannelData, boolean newState) throws ThingException {
		final int relayChannelPin = relayChannelData.getPin();
		GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalOutput relayChannel = gpio
				.provisionDigitalOutputPin(RaspiPin.getPinByAddress(relayChannelPin), relayChannelData.getName());
		relayChannel.setState(newState);
		gpio.shutdown();
		gpio.unprovisionPin(relayChannel);
		IoTLogger.getInstance().info("State of relay channel: " + relayChannelData.getName() + " set to " + newState);
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
		final RelayBoards relayBoards = RelayConfigReader.getRelayBoards();
		if (relayBoards != null) {
			List<RelayBoard> relayBoardList = relayBoards.getRelayBoards();
			if (!relayBoardList.isEmpty()) {
				// Loop all relay boards
				for (RelayBoard relayBoard : relayBoardList) {
					List<RelayChannel> relayChannels = relayBoard.getRelayChannels();
					if (!relayChannels.isEmpty()) {
						// Loop all channels within each relay board
						for (RelayChannel relayChannel : relayChannels) {
							// set relay channel in OFF state
							if (relayChannel.getRelayType() == RelayType.NO) {
								setChannelState(relayChannel, true);
							} else {
								setChannelState(relayChannel, false);
							}
						}
					}
				}
			}
		}

	}

}
