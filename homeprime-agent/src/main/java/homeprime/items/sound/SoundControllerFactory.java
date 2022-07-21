package homeprime.items.sound;

import homeprime.core.exception.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.items.sound.raspberrypi.SoundControllerImpl;

/**
 * Thing sound control factory.
 * 
 * @author Milan Ramljak
 */
public class SoundControllerFactory {

	/**
	 * Hidden constructor.
	 */
	private SoundControllerFactory() {
	}

	public static SoundController getSoundController() throws ThingException {
		switch (ThingProperties.getInstance().getThingSystemType()) {
		case RaspberryPi:
			return SoundControllerImpl.getInstance();
		case BananaPi:
			throw new ThingException("Thing sound control for BannanaPI note supported yet.");
		case BeagleBoneBlack:
			throw new ThingException("Thing sound control for BeagleBoneBlack note supported yet.");
		case Mock:
			return new homeprime.items.sound.mock.SoundControllerImpl();
		default:
			throw new ThingException("Thing sound control implementation for Unknown cannot be created.");
		}
	}

}
