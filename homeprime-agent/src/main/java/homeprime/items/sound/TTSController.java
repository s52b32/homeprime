package homeprime.items.sound;

import homeprime.core.exception.ThingException;
import homeprime.items.sound.tts.config.pojos.TtsConfig;

/**
 * Interface for TTS (Text To Speech) operations.
 *
 * @author Milan Ramljak
 *
 */
public interface TTSController {

    /**
     * Utilize TTS.
     *
     * @param ttsconfig - TTS config system
     * @param text      - text to speak
     * @return {@code true} if successful
     * @throws ThingException
     */
    Boolean speak(TtsConfig ttsconfig, String text) throws ThingException;
}
