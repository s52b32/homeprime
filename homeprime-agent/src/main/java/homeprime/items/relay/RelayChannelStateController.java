package homeprime.items.relay;

import homeprime.core.exceptions.ThingException;
import homeprime.items.relay.config.pojos.RelayChannel;

/**
 * Management interface for relay channel state operations.
 * 
 * @author Milan Ramljak
 */
public interface RelayChannelStateController {

	/**
	 * Read relay channel status of provided channel.
	 * 
	 * @param relayChannelData relay channel data
	 * @return state of pin connected to relay channel
	 */
	Boolean readChannelState(RelayChannel relayChannelData) throws ThingException;

	/**
	 * Toggle relay channel state of provided channel.
	 * 
	 * @param relayChannelData relay channel data
	 */
	void toggleChannelState(RelayChannel relayChannelData) throws ThingException;

	/**
	 * Change relay channel state of provided channel.
	 * 
	 * @param relayChannelData relay channel data
	 * @param newState         state to be set to channel pin
	 */
	void setChannelState(RelayChannel relayChannelData, boolean newState) throws ThingException;

	/**
	 * Initialize default states for relay related pins. If relay channel is in NC
	 * mode, default state will result that connected device is in OFF state i.e.
	 * disconnected.
	 * 
	 * @throws ThingException in case of failure
	 */
	void initialize() throws ThingException;

}
