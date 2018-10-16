package homeprime.items.sound;

import homeprime.core.exceptions.ThingException;

/**
 * Interface for contact state reading.
 * 
 * @author Milan Ramljak
 * 
 */
public interface SoundController {

    /**
     * Read current volume.
     * 
     * @return current volume. Range is 0-100.
     * @throws ThingException
     *             in case of error while reading volume
     */
    Integer readVolume() throws ThingException;

    /**
     * Set volume.
     * 
     * @param volume
     *            volume to be set in range 0-100.
     * @return {@code true} if successful
     * @throws ThingException
     *             in case of error while setting volume
     */
    Boolean setVolume(Integer volume) throws ThingException;

    /**
     * Utilize TTS.
     * 
     * @param text
     *            text to speak
     * @return {@code true} if successful
     * @throws ThingException
     */
    Boolean speak(String text) throws ThingException;

}
