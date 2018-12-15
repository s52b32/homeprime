package homeprime.items.relay;

import homeprime.core.exceptions.ThingException;
import homeprime.items.relay.config.pojos.Relay;

/**
 * Management interface for relay state operations.
 * 
 * @author Milan Ramljak
 */
public interface RelayStateController {

	/**
	 * Read relay status of provided relay.
	 * 
	 * @param relayData relay data
	 * @return state of pin connected to relay
	 */
	Boolean readState(Relay relayData) throws ThingException;

	/**
	 * Toggle relay state of provided relay.
	 * 
	 * @param relayData relay data
	 */
	void toggleState(Relay relayData) throws ThingException;

	/**
	 * Change relay state of provided relay.
	 * 
	 * @param relayData relay data
	 * @param newState  state to be set to channel pin
	 */
	void setState(Relay relayData, boolean newState) throws ThingException;

	/**
	 * Initialize default states for relay related pins. If relay is in NC mode,
	 * default state will result that connected device is in OFF state i.e.
	 * disconnected.
	 * 
	 * @throws ThingException in case of failure
	 */
	void initialize() throws ThingException;

}
