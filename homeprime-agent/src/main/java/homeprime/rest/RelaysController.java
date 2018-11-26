package homeprime.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.exceptions.ThingException;
import homeprime.items.relay.RelayChannelStateControllerFactory;
import homeprime.items.relay.config.enums.RelayChannelState;
import homeprime.items.relay.config.pojos.RelayBoard;
import homeprime.items.relay.config.pojos.RelayBoards;
import homeprime.items.relay.config.pojos.RelayChannel;
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
	public ResponseEntity<RelayBoards> getRelayInfo() {
		try {
			return new ResponseEntity<RelayBoards>(RelayConfigReader.getRelayBoards(), HttpStatus.OK);
		} catch (ThingException e) {
			return new ResponseEntity<RelayBoards>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Relays/sync")
	public ResponseEntity<String> syncRelayConfig() {
		RelayConfigReader.resyncConfig();
		return new ResponseEntity<String>("Relay config re-sync scheduled", HttpStatus.OK);
	}

	@RequestMapping("/Thing/Relay/{relayBoardId}")
	public ResponseEntity<RelayBoard> getRelayBoardById(@PathVariable(value = "relayBoardId") int relayBoardId) {
		try {
			RelayBoard findRelayBoardById = findRelayBoardById(relayBoardId);
			if (findRelayBoardById != null) {
				return new ResponseEntity<RelayBoard>(findRelayBoardById(relayBoardId), HttpStatus.OK);
			} else {
				return new ResponseEntity<RelayBoard>(HttpStatus.NOT_FOUND);
			}

		} catch (ThingException e) {
			return new ResponseEntity<RelayBoard>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Relay/{relayBoardId}/Channels")
	public ResponseEntity<List<RelayChannel>> getRelayBoardChannels(
			@PathVariable(value = "relayBoardId") int relayBoardId) {
		try {
			final RelayBoard findRelayBoardById = findRelayBoardById(relayBoardId);
			if (findRelayBoardById != null) {
				return new ResponseEntity<List<RelayChannel>>(findRelayBoardById(relayBoardId).getRelayChannels(),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<List<RelayChannel>>(HttpStatus.NOT_FOUND);
			}

		} catch (ThingException e) {
			return new ResponseEntity<List<RelayChannel>>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Relay/{relayBoardId}/Channel/{relayBoardChannelId}")
	public ResponseEntity<RelayChannel> getRelayBoardChannelById(@PathVariable(value = "relayBoardId") int relayBoardId,
			@PathVariable(value = "relayBoardChannelId") int relayBoardChannelId) {
		try {
			final RelayBoard findRelayBoardById = findRelayBoardById(relayBoardId);
			if (findRelayBoardById != null && findRelayBoardById.getRelayChannels().size() >= relayBoardChannelId) {
				return new ResponseEntity<RelayChannel>(
						findRelayBoardById(relayBoardId).getRelayChannels().get(relayBoardChannelId), HttpStatus.OK);
			} else {
				return new ResponseEntity<RelayChannel>(HttpStatus.NOT_FOUND);
			}

		} catch (ThingException e) {
			return new ResponseEntity<RelayChannel>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Relay/{relayBoardId}/Channel/{relayBoardChannelId}/read")
	public ResponseEntity<String> getRelayBoardChannelState(@PathVariable(value = "relayBoardId") int relayBoardId,
			@PathVariable(value = "relayBoardChannelId") int relayBoardChannelId) {
		try {
			final RelayBoard findRelayBoard = findRelayBoardById(relayBoardId);
			if (findRelayBoard != null) {
				final RelayChannel relayChannel = findRelayChannelById(findRelayBoard, relayBoardChannelId);
				if (relayChannel != null) {
					final Boolean currentChannelStatus = RelayChannelStateControllerFactory.getRelayChannelStateReader()
							.readChannelState(relayChannel);
					return new ResponseEntity<String>(getRelayChannelState(currentChannelStatus), HttpStatus.OK);
				} else {
					return new ResponseEntity<String>(
							"Relay channel " + relayBoardChannelId + " doesn't exist in relay board " + relayBoardId,
							HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<String>(
						"Relay board with id: " + relayBoardId + " not found, cannot proceed with state read",
						HttpStatus.NOT_FOUND);
			}

		} catch (ThingException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Relay/{relayBoardId}/Channel/{relayBoardChannelId}/toggle")
	public ResponseEntity<String> toggleRelayBoardChannelState(@PathVariable(value = "relayBoardId") int relayBoardId,
			@PathVariable(value = "relayBoardChannelId") int relayBoardChannelId) {
		try {
			final RelayBoard findRelayBoard = findRelayBoardById(relayBoardId);
			if (findRelayBoard != null) {
				final RelayChannel relayChannel = findRelayChannelById(findRelayBoard, relayBoardChannelId);
				if (relayChannel != null) {
					RelayChannelStateControllerFactory.getRelayChannelStateReader().toggleChannelState(relayChannel);
					return new ResponseEntity<String>("Relay channel state toggled", HttpStatus.OK);
				} else {
					return new ResponseEntity<String>(
							"Relay channel " + relayBoardChannelId + " doesn't exist in relay board " + relayBoardId,
							HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<String>(
						"Relay board with id: " + relayBoardId + " not found, cannot proceed with toggle",
						HttpStatus.NOT_FOUND);
			}

		} catch (ThingException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Relay/{relayBoardId}/Channel/{relayBoardChannelId}/on")
	public ResponseEntity<String> powerOnRelayBoardChannel(@PathVariable(value = "relayBoardId") int relayBoardId,
			@PathVariable(value = "relayBoardChannelId") int relayBoardChannelId) {
		try {
			final RelayBoard findRelayBoard = findRelayBoardById(relayBoardId);
			if (findRelayBoard != null) {
				final RelayChannel relayChannel = findRelayChannelById(findRelayBoard, relayBoardChannelId);
				if (relayChannel != null) {
					RelayChannelStateControllerFactory.getRelayChannelStateReader().setChannelState(relayChannel,
							false);
					return new ResponseEntity<String>("Relay channel pin state set to ON", HttpStatus.OK);
				} else {
					return new ResponseEntity<String>(
							"Relay channel " + relayBoardChannelId + " doesn't exist in relay board " + relayBoardId,
							HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<String>(
						"Relay board with id: " + relayBoardId + " not found, cannot proceed with power on",
						HttpStatus.NOT_FOUND);
			}

		} catch (ThingException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Relay/{relayBoardId}/Channel/{relayBoardChannelId}/off")
	public ResponseEntity<String> powerOffRelayBoardChannel(@PathVariable(value = "relayBoardId") int relayBoardId,
			@PathVariable(value = "relayBoardChannelId") int relayBoardChannelId) {
		try {
			final RelayBoard findRelayBoard = findRelayBoardById(relayBoardId);
			if (findRelayBoard != null) {
				final RelayChannel relayChannel = findRelayChannelById(findRelayBoard, relayBoardChannelId);
				if (relayChannel != null) {
					RelayChannelStateControllerFactory.getRelayChannelStateReader().setChannelState(relayChannel, true);
					return new ResponseEntity<String>("Relay channel pin state set to OFF", HttpStatus.OK);
				} else {
					return new ResponseEntity<String>(
							"Relay channel " + relayBoardChannelId + " doesn't exist in relay board " + relayBoardId,
							HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<String>(
						"Relay board with id: " + relayBoardId + " not found, cannot proceed with power off",
						HttpStatus.NOT_FOUND);
			}

		} catch (ThingException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Helper method for relay channel state definition.
	 * 
	 * @param currentRelayChannelStatus - value of channel status reader
	 * @return string representing relay channel state as defined in
	 *         {@link RelayChannelState}
	 */
	private String getRelayChannelState(Boolean currentRelayChannelStatus) {
		if (currentRelayChannelStatus == null) {
			return RelayChannelState.UNKNOWN.toString();
		} else if (currentRelayChannelStatus == true) {
			return RelayChannelState.ON.toString();
		} else {
			return RelayChannelState.OFF.toString();
		}
	}

	/**
	 * Helper method to find relay board by id.
	 * 
	 * @param relayBoardId relay board id
	 * @return {@link RelayBoard}
	 * @throws ThingException in case of error
	 */
	private RelayBoard findRelayBoardById(int relayBoardId) throws ThingException {
		List<RelayBoard> relayBoards = new ArrayList<RelayBoard>();
		try {
			relayBoards = RelayConfigReader.getRelayBoards().getRelayBoards();
		} catch (ThingException e) {
			throw new ThingException(
					"ERROR ThingRelayController.findRelayBoardById(" + relayBoardId + ") Failed to get relay boards.",
					e);
		}

		if (!relayBoards.isEmpty()) {
			for (RelayBoard relayBoard : relayBoards) {
				if (relayBoard.getId() == relayBoardId) {
					return relayBoard;
				}
			}
		}
		return null;
	}

	/**
	 * Helper method to find relay channel by id.
	 * 
	 * @param relayBoard relay board data object
	 * @return {@link RelayChannel}
	 * @throws ThingException in case of error
	 */
	private RelayChannel findRelayChannelById(RelayBoard relayBoard, int relayBoardChannelId) throws ThingException {
		if (relayBoard != null) {
			List<RelayChannel> relayChannels = relayBoard.getRelayChannels();
			if (!relayChannels.isEmpty()) {
				for (RelayChannel relayChannel : relayChannels) {
					if (relayChannel.getId() == relayBoardChannelId) {
						return relayChannel;
					}
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
		throw new ThingException("ERROR ThingRelayController.findRelayChannelById(" + relayBoardChannelId
				+ ") Did not find relay channel with required id " + relayBoardChannelId);

	}
}
