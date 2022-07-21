package homeprime.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.exception.ThingException;
import homeprime.core.model.readers.items.relay.RelayConfigReader;
import homeprime.items.relay.RelayOperation;
import homeprime.items.relay.RelayStateControllerFactory;
import homeprime.items.relay.config.pojos.Relay;
import homeprime.items.relay.config.pojos.Relays;

/**
 * Spring REST controller for thing relay setup.
 *
 * @author Milan Ramljak
 *
 */
@RestController
public class RelaysController {

    @RequestMapping(value = "/Thing/Relays/reload-config", method = RequestMethod.GET)
    public ResponseEntity<String> syncRelayConfig() {
        RelayConfigReader.reloadConfig();
        return new ResponseEntity<String>("Relay config re-sync scheduled", HttpStatus.OK);
    }

    @RequestMapping(value = "/Thing/Relays", method = RequestMethod.GET)
    public ResponseEntity<Relays> getRelayOverview() {
        Relays relays = null;
        try {
            relays = RelayConfigReader.getRelays();

            if (relays != null) {
                final List<Relay> relaysList = relays.getRelays();
                if (!relaysList.isEmpty()) {
                    for (Relay relay : relaysList) {
                        relay.setState(RelayStateControllerFactory.getRelayStateReader().readState(relay));
                    }
                }
            }
        } catch (ThingException e) {
            return new ResponseEntity<Relays>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Relays>(relays, HttpStatus.OK);
    }

    @RequestMapping(value = "/Thing/Relays/{relayId}", method = RequestMethod.POST)
    public ResponseEntity<?> getRelayById(@PathVariable(value = "relayId") int relayId,
            @RequestBody String relayOperation) {

        if (RelayOperation.fromText(relayOperation) == null) {
            return new ResponseEntity<String>(
                    "Missing or invalid request body for POST operation. Allowed: " + RelayOperation.listValues(),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            Relay relay = findRelayById(relayId);
            if (relay == null) {
                return new ResponseEntity<String>(
                        "Relay with provided is (" + relayId + ") doesn't exist in running configuration!",
                        HttpStatus.NOT_FOUND);
            }
            Boolean result = null;
            switch (RelayOperation.fromText(relayOperation)) {
                case on:
                    result = powerOnRelay(relay);
                    break;
                case off:
                    result = powerOffRelay(relay);
                    break;
                case toggle:
                    result = toggleRelayState(relay);
                    break;
                default:
                    return new ResponseEntity<String>(
                            "Provided relay operation (" + relayOperation + ") is not implemented!",
                            HttpStatus.NOT_IMPLEMENTED);
            }
            if (result != null && result != false) {
                relay.setState(RelayStateControllerFactory.getRelayStateReader().readState(relay));
                // success
                return new ResponseEntity<Relay>(relay, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Failed to change relay (" + relayId + ") state",
                        HttpStatus.BAD_REQUEST);
            }

        } catch (ThingException e) {
            return new ResponseEntity<String>("Exception happen while changing relay state!", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/Thing/Relays/{relayId}/on")
    public ResponseEntity<?> relaySetOn(@PathVariable(value = "relayId") int relayId) {

        try {
            Relay relay = findRelayById(relayId);
            if (relay == null) {
                return new ResponseEntity<String>(
                        "Relay with provided is (" + relayId + ") doesn't exist in running configuration!",
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Boolean>(powerOnRelay(relay), HttpStatus.OK);

        } catch (ThingException e) {
            return new ResponseEntity<String>("Exception happen while changing relay state!", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/Thing/Relays/{relayId}/off")
    public ResponseEntity<?> relaySetOff(@PathVariable(value = "relayId") int relayId) {

        try {
            Relay relay = findRelayById(relayId);
            if (relay == null) {
                return new ResponseEntity<String>(
                        "Relay with provided is (" + relayId + ") doesn't exist in running configuration!",
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Boolean>(powerOffRelay(relay), HttpStatus.OK);

        } catch (ThingException e) {
            return new ResponseEntity<String>("Exception happen while changing relay state!", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/Thing/Relays/{relayId}/read")
    public ResponseEntity<?> relayReadState(@PathVariable(value = "relayId") int relayId) {

        try {
            Relay relay = findRelayById(relayId);
            if (relay == null) {
                return new ResponseEntity<String>(
                        "Relay with provided is (" + relayId + ") doesn't exist in running configuration!",
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(
                    RelayStateControllerFactory.getRelayStateReader().readState(relay) ? "ON" : "OFF", HttpStatus.OK);

        } catch (ThingException e) {
            return new ResponseEntity<String>("Exception happen while changing relay state!", HttpStatus.BAD_REQUEST);
        }
    }

    public Boolean toggleRelayState(Relay relay) throws ThingException {
        if (relay != null) {
            RelayStateControllerFactory.getRelayStateReader().toggleState(relay);
            return true;
        } else {
            return false;
        }
    }

    public Boolean powerOnRelay(Relay relay) throws ThingException {
        if (relay != null) {
            RelayStateControllerFactory.getRelayStateReader().setState(relay, false);
            return true;
        } else {
            return false;
        }

    }

    public Boolean powerOffRelay(Relay relay) throws ThingException {
        if (relay != null) {
            RelayStateControllerFactory.getRelayStateReader().setState(relay, true);
            return true;
        } else {
            return false;
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
