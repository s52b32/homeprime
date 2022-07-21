package homeprime.items.sound;

import homeprime.core.exception.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.items.sound.raspberrypi.TTSControllerImpl;

/**
 * Thing TTS factory.
 *
 * @author Milan Ramljak
 */
public class TTSControllerFactory {

    /**
     * Hidden constructor.
     */
    private TTSControllerFactory() {
    }

    public static TTSController getTTSController() throws ThingException {
        switch (ThingProperties.getInstance().getThingSystemType()) {
            case RaspberryPi:
                return TTSControllerImpl.getInstance();
            case BananaPi:
                throw new ThingException("Thing TTS for BannanaPI note supported yet.");
            case BeagleBoneBlack:
                throw new ThingException("Thing TTS for BeagleBoneBlack note supported yet.");
            case Mock:
                return new homeprime.items.sound.mock.TTSControllerImpl();
            default:
                throw new ThingException("Thing TTS implementation for Unknown cannot be created.");
        }
    }

}
