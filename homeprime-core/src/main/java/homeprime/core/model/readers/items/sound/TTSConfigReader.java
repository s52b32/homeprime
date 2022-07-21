package homeprime.core.model.readers.items.sound;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.core.exception.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.core.utils.ThingUtils;
import homeprime.items.sound.tts.config.pojos.TtsConfigs;

/**
 * TTS configuration loader.
 *
 * @author Milan Ramljak
 *
 */
public class TTSConfigReader {
    /**
     * File describing TTS configuration.
     */
    public static final String TTS_CONFIGURATION_FILE_NAME = "tts.json";
    private static String tts = null;

    /**
     * Hidden constructor.
     */
    private TTSConfigReader() {
    }

    public static TtsConfigs getTts() throws ThingException {

        try {
            if (tts == null) {
                tts = ThingUtils
                        .readFile(ThingProperties.getInstance().getItemsRootPath() + TTS_CONFIGURATION_FILE_NAME);
            }
            final ObjectMapper mapper = new ObjectMapper();
            final TtsConfigs ttsConfigsPojo = mapper.readValue(tts, TtsConfigs.class);
            return ttsConfigsPojo;
        } catch (Exception e) {
            throw new ThingException("ERROR TTSConfigReader.getSounds() Failed to parse " + TTS_CONFIGURATION_FILE_NAME,
                    e);
        }
    }

    /**
     * Force that next getTts method call reads configuration again.
     */
    public static void reloadConfig() {
        tts = null;
    }

}
