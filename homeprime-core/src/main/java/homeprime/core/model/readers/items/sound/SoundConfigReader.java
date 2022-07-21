package homeprime.core.model.readers.items.sound;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.core.exception.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.core.utils.ThingUtils;
import homeprime.items.sound.config.pojos.Sound;

/**
 * Sound configuration loader.
 *
 * @author Milan Ramljak
 *
 */
public class SoundConfigReader {
    /**
     * File describing temperature sensor items.
     */
    public static final String SOUND_CONFIGURATION_FILE_NAME = "sound.json";
    private static String sound = null;

    /**
     * Hidden constructor.
     */
    private SoundConfigReader() {
    }

    public static Sound getSound() throws ThingException {

        try {
            if (sound == null) {
                sound = ThingUtils
                        .readFile(ThingProperties.getInstance().getItemsRootPath() + SOUND_CONFIGURATION_FILE_NAME);
            }
            final ObjectMapper mapper = new ObjectMapper();
            final Sound soundPojo = mapper.readValue(sound, Sound.class);
            return soundPojo;
        } catch (Exception e) {
            throw new ThingException(
                    "ERROR SoundConfigReader.getSounds() Failed to parse " + SOUND_CONFIGURATION_FILE_NAME, e);
        }
    }

    /**
     * Force that next getSound method call reads configuration again.
     */
    public static void reloadConfig() {
        sound = null;
    }

}
