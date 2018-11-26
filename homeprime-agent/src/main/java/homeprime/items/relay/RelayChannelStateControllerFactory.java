package homeprime.items.relay;

import homeprime.core.exceptions.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.items.relay.raspberrypi.RelayChannelStateControllerImpl;

/**
 * Thing relay channel state reader.
 * 
 * @author Milan Ramljak
 */
public class RelayChannelStateControllerFactory {

	/**
	 * Hidden constructor.
	 */
	private RelayChannelStateControllerFactory() {
	}

	public static RelayChannelStateController getRelayChannelStateReader() throws ThingException {
		switch (ThingProperties.getInstance().getThingSystemType()) {
		case RaspberryPi:
			return new RelayChannelStateControllerImpl();
		case BananaPi:
			throw new ThingException("Thing relay channel state reader for BannanaPI note supported yet.");
		case BeagleBoneBlack:
			throw new ThingException("Thing relay channel state reader for BeagleBoneBlack note supported yet.");
		case Mock:
			return new homeprime.items.relay.mock.RelayChannelStateControllerImpl();
		default:
			throw new ThingException(
					"Thing relay channel state reader implementation for Unknown system type cannot be created.");
		}
	}

}
