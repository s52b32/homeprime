package homeprime.items.relay.config.reader;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.core.exceptions.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.core.utils.ThingUtils;
import homeprime.items.relay.config.pojos.RelayBoards;

/**
 * Relay settings loader.
 * 
 * @author Milan Ramljak
 * 
 */
public class RelayConfigReader {

	private static String relayBoards = null;

	/**
	 * Hidden constructor.
	 */
	private RelayConfigReader() {
	}

	public static RelayBoards getRelayBoards() throws ThingException {

		try {
			if (relayBoards == null) {
				relayBoards = ThingUtils
						.readFile(ThingProperties.getInstance().getThingConfigPath() + "relay_board.json");
			}
			final ObjectMapper mapper = new ObjectMapper();
			final RelayBoards relayBoardsPojo = mapper.readValue(relayBoards, RelayBoards.class);
			return relayBoardsPojo;
		} catch (IOException e) {
			throw new ThingException("RelayInfo.getRelayBoards() Failed to parse relay_boards.json", e);
		}
	}

	/**
	 * Force that next getRelayBoards method call reads configuration again.
	 */
	public static void resyncConfig() {
		relayBoards = null;
	}

}
