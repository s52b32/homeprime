package homeprime.manager.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import homeprime.agent.client.AgentRestApiClient;
import homeprime.agent.config.pojos.Thing;
import homeprime.agent.config.pojos.Things;
import homeprime.core.exception.ThingException;
import homeprime.core.model.readers.config.ConfigurationReader;

/**
 * Spring REST controller for HomePrime agent remote management.
 *
 * @author Milan Ramljak
 */
@RestController
public class AgentProxyController {

    @GetMapping("/Things/{uuid}/System")
    public ResponseEntity<?> forwarderThing(@PathVariable(value = "uuid", required = true) String uuid)
            throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        if (thingConfigurations.getThings().isEmpty()) {
            // this means that no things exist
            return new ResponseEntity<String>("List of things is empty, not possible to query remote agent",
                    HttpStatus.NO_CONTENT);
        } else {
            // check thing with uuid exists
            final Thing thing = getThingById(thingConfigurations.getThings(), uuid);
            if (thing != null) {
                final String thingResponse = AgentRestApiClient.getThingSystemInfo(thing);
                if (thingResponse != null) {
                    return new ResponseEntity<String>(thingResponse, HttpStatus.OK);
                } else {
                    // this means that thing didn't respond
                    return new ResponseEntity<String>("Thing " + uuid + " failed to respond", HttpStatus.BAD_REQUEST);
                }
            } else {
                // this means that thing with provided UUID doesn't exist
                return new ResponseEntity<String>("Thing " + uuid + " cannot be queried, does not exist",
                        HttpStatus.NOT_FOUND);
            }
        }
    }

    private Thing getThingById(List<Thing> things, String thingUuid) {
        if (thingUuid != null) {
            for (Thing thingItem : things) {
                // make sure things are unique by UUID
                if (thingItem.getUuid().equals(thingUuid)) {
                    return thingItem;
                }
            }
        }
        return null;
    }

}
