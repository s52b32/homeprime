package homeprime.core.model.readers.items.relay;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.core.exception.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.core.utils.ThingUtils;
import homeprime.items.relay.config.pojos.Relays;

/**
 * Relay settings loader.
 *
 * @author Milan Ramljak
 *
 */
public class RelayConfigReader {

    /**
     * File describing relay items.
     */
    public static final String RELAYS_CONFIGURATION_FILE_NAME = "relays.json";
    private static String relays = null;

    /**
     * Hidden constructor.
     */
    private RelayConfigReader() {
    }

    public static Relays getRelays() throws ThingException {

        try {
            if (relays == null) {
                relays = ThingUtils
                        .readFile(ThingProperties.getInstance().getItemsRootPath() + RELAYS_CONFIGURATION_FILE_NAME);
            }
            final ObjectMapper mapper = new ObjectMapper();
            final Relays relaysPojo = mapper.readValue(relays, Relays.class);
            return relaysPojo;
        } catch (IOException e) {
            throw new ThingException("RelayConfigReader.getRelays() Failed to parse " + RELAYS_CONFIGURATION_FILE_NAME,
                    e);
        }
    }

    /**
     * Force that next getRelayBoards method call reads configuration again.
     */
    public static void reloadConfig() {
        relays = null;
    }

}
