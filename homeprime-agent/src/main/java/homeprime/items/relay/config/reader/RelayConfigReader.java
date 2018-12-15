package homeprime.items.relay.config.reader;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.core.exceptions.ThingException;
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

	private static String relays = null;

	/**
	 * Hidden constructor.
	 */
	private RelayConfigReader() {
	}

	public static Relays getRelays() throws ThingException {

		try {
			if (relays == null) {
				relays = ThingUtils.readFile(ThingProperties.getInstance().getThingConfigPath() + "relays.json");
			}
			final ObjectMapper mapper = new ObjectMapper();
			final Relays relaysPojo = mapper.readValue(relays, Relays.class);
			return relaysPojo;
		} catch (IOException e) {
			throw new ThingException("RelayInfo.getRelays() Failed to parse relay.json", e);
		}
	}

	/**
	 * Force that next getRelayBoards method call reads configuration again.
	 */
	public static void resyncConfig() {
		relays = null;
	}

}
