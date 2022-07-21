package homeprime.items.sound.mock;

import homeprime.core.exception.ThingException;
import homeprime.items.sound.TTSController;
import homeprime.items.sound.tts.config.pojos.TtsConfig;

/**
 * Implementation of TTS for Mock.
 *
 * @author Milan Ramljak
 */
public class TTSControllerImpl implements TTSController {

    private static TTSControllerImpl singleton = null;

    public static TTSControllerImpl getInstance() throws ThingException {
        if (singleton == null) {
            singleton = new TTSControllerImpl();
        }
        return singleton;
    }

    @Override
    public Boolean speak(TtsConfig ttsConfig, String text) throws ThingException {
        return true;
    }

}
