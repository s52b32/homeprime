package homeprime.items.relay;

import homeprime.core.exceptions.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.items.relay.raspberrypi.RelayStateControllerImpl;

/**
 * Thing relay channel state reader.
 * 
 * @author Milan Ramljak
 */
public class RelayStateControllerFactory {

	/**
	 * Hidden constructor.
	 */
	private RelayStateControllerFactory() {
	}

	public static RelayStateController getRelayStateReader() throws ThingException {
		switch (ThingProperties.getInstance().getThingSystemType()) {
		case RaspberryPi:
			return new RelayStateControllerImpl();
		case BananaPi:
			throw new ThingException("Thing relay channel state reader for BannanaPI note supported yet.");
		case BeagleBoneBlack:
			throw new ThingException("Thing relay channel state reader for BeagleBoneBlack note supported yet.");
		case Mock:
			return new homeprime.items.relay.mock.RelayStateControllerImpl();
		default:
			throw new ThingException(
					"Thing relay channel state reader implementation for Unknown system type cannot be created.");
		}
	}

}
