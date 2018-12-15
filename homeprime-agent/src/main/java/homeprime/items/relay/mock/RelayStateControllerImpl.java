package homeprime.items.relay.mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import homeprime.core.exceptions.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.items.relay.RelayStateController;
import homeprime.items.relay.config.enums.RelayType;
import homeprime.items.relay.config.pojos.Relay;
import homeprime.items.relay.config.pojos.Relays;
import homeprime.items.relay.config.reader.RelayConfigReader;

/**
 * Manage relay channel pin state connected as mocked system.
 * 
 * @author Milan Ramljak
 */
public class RelayStateControllerImpl implements RelayStateController {

	private static Map<Integer, Boolean> portStates = new HashMap<Integer, Boolean>();

	@Override
	public Boolean readState(Relay relayData) throws ThingException {
		if (relayData == null) {
			throw new ThingException(
					"ERROR RelayStateControllerImpl.readState() Cannot check relay state if Relay pojo is null");
		}
		final int relayPin = relayData.getPin();
		Boolean state = portStates.get(relayPin);

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
		Boolean currentState = portStates.get(relayPin);
		portStates.put(relayPin, !currentState);
		IoTLogger.getInstance().info("Toggling current state of relay: " + relayData.getName());
	}

	@Override
	public void setState(Relay relayData, boolean newState) throws ThingException {
		final int relayPin = relayData.getPin();
		portStates.put(relayPin, newState);
		IoTLogger.getInstance().info("State of relay: " + relayData.getName() + " set to " + newState);
	}

	@Override
	public void initialize() throws ThingException {
		// TODO: investigate do we need this?
		final Relays relays = RelayConfigReader.getRelays();
		if (relays != null) {
			List<Relay> relaies = relays.getRelays();
			if (!relaies.isEmpty()) {
				for (Relay relay : relaies) {
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
