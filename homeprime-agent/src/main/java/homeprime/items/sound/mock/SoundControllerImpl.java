package homeprime.items.sound.mock;

import homeprime.core.exception.ThingException;
import homeprime.items.sound.SoundController;
import homeprime.items.sound.raspberrypi.StreamingControllerImpl;

/**
 * Implementation of sound control for moced thing.
 *
 * @author Milan Ramljak
 */
public class SoundControllerImpl implements SoundController {

    private static Integer volume = 100;
    private static SoundControllerImpl singleton = null;

    public static SoundControllerImpl getInstance() throws ThingException {
        if (singleton == null) {
            singleton = new SoundControllerImpl();
        }
        return singleton;
    }

    @Override
    public Integer readVolume() throws ThingException {
        return volume;
    }

    @Override
    public Boolean setVolume(Integer volume) throws ThingException {
        SoundControllerImpl.volume = volume;
        return true;
    }

    @Override
    public StreamingControllerImpl stream() throws ThingException {
        return StreamingControllerImpl.getInstance();
    }

}
