package homeprime.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.exceptions.ThingException;
import homeprime.items.relay.RelayStateControllerFactory;
import homeprime.items.relay.config.enums.RelayState;
import homeprime.items.relay.config.pojos.Relay;
import homeprime.items.relay.config.pojos.Relays;
import homeprime.items.relay.config.reader.RelayConfigReader;

/**
 * Spring REST controller for thing relay setup.
 * 
 * @author Milan Ramljak
 * 
 */
@RestController
public class RelaysController {

	@RequestMapping("/Thing/Relays")
	public ResponseEntity<Relays> getRelayInfo() {
		try {
			return new ResponseEntity<Relays>(RelayConfigReader.getRelays(), HttpStatus.OK);
		} catch (ThingException e) {
			return new ResponseEntity<Relays>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Relays/sync")
	public ResponseEntity<String> syncRelayConfig() {
		RelayConfigReader.resyncConfig();
		return new ResponseEntity<String>("Relay config re-sync scheduled", HttpStatus.OK);
	}

	@RequestMapping("/Thing/Relays/{relayId}")
	public ResponseEntity<Relay> getRelayById(@PathVariable(value = "relayId") int relayId) {
		try {
			Relay findRelayById = findRelayById(relayId);
			if (findRelayById != null) {
				return new ResponseEntity<Relay>(findRelayById, HttpStatus.OK);
			} else {
				return new ResponseEntity<Relay>(HttpStatus.NOT_FOUND);
			}

		} catch (ThingException e) {
			return new ResponseEntity<Relay>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Relays/{relayId}/read")
	public ResponseEntity<String> getRelayBoardChannelState(@PathVariable(value = "relayId") int relayId) {
		try {
			final Relay relay = findRelayById(relayId);
			if (relay != null) {
				final Boolean currentChannelStatus = RelayStateControllerFactory.getRelayStateReader().readState(relay);
				return new ResponseEntity<String>(getRelayState(currentChannelStatus), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Relay with id '" + relayId + "' doesn't exist!",
						HttpStatus.NOT_FOUND);
			}

		} catch (ThingException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Relays/{relayId}/toggle")
	public ResponseEntity<String> toggleRelayState(@PathVariable(value = "relayId") int relayId) {
		try {
			final Relay relay = findRelayById(relayId);
			if (relay != null) {
				RelayStateControllerFactory.getRelayStateReader().toggleState(relay);
				return new ResponseEntity<String>("Relay state toggled", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Relay with id '" + relayId + "' doesn't exist!",
						HttpStatus.NOT_FOUND);
			}

		} catch (ThingException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Relays/{relayId}/on")
	public ResponseEntity<String> powerOnRelayBoardChannel(@PathVariable(value = "relayId") int relayId) {
		try {
			final Relay relay = findRelayById(relayId);
			if (relay != null) {
				RelayStateControllerFactory.getRelayStateReader().setState(relay, false);
				return new ResponseEntity<String>("Relay pin state set to ON", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Relay with id " + relayId + " doesn't exist!", HttpStatus.NOT_FOUND);
			}

		} catch (ThingException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Relays/{relayId}/off")
	public ResponseEntity<String> powerOffRelay(@PathVariable(value = "relayId") int relayId) {
		try {
			final Relay relay = findRelayById(relayId);
			if (relay != null) {
				RelayStateControllerFactory.getRelayStateReader().setState(relay, true);
				return new ResponseEntity<String>("Relay pin state set to OFF", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Relay with id " + relayId + " doesn't exist!", HttpStatus.NOT_FOUND);
			}

		} catch (ThingException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Helper method for relay state definition.
	 * 
	 * @param currentRelayStatus - value of relay status reader
	 * @return string representing relay state as defined in {@link RelayState}
	 */
	private String getRelayState(Boolean currentRelayStatus) {
		if (currentRelayStatus == null) {
			return RelayState.UNKNOWN.toString();
		} else if (currentRelayStatus == true) {
			return RelayState.ON.toString();
		} else {
			return RelayState.OFF.toString();
		}
	}

	/**
	 * Helper method to find relay by id.
	 * 
	 * @param relayBoardId relay id
	 * @return {@link Relay}
	 * @throws ThingException in case of error
	 */
	private Relay findRelayById(int relayId) throws ThingException {
		List<Relay> relays = new ArrayList<Relay>();
		try {
			relays = RelayConfigReader.getRelays().getRelays();
		} catch (ThingException e) {
			throw new ThingException("ERROR ThingRelayController.findRelayById(" + relayId + ") Failed to get relays.",
					e);
		}

		if (!relays.isEmpty()) {
			for (Relay relay : relays) {
				if (relay.getId() == relayId) {
					return relay;
				}
			}
		}
		return null;
	}
}
