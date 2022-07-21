package homeprime.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.exception.ThingException;
import homeprime.core.model.readers.items.sound.TTSConfigReader;
import homeprime.items.sound.TTSControllerFactory;
import homeprime.items.sound.tts.config.pojos.TtsConfig;
import homeprime.items.sound.tts.config.pojos.TtsConfigs;

/**
 * Spring REST controller for thing TTS.
 *
 * @author Milan Ramljak
 *
 */
@RestController
public class TTSController {

    @RequestMapping(value = "/Thing/TTS", method = RequestMethod.GET)
    public ResponseEntity<TtsConfigs> getTTSOverview() {
        TtsConfigs ttsConfigs = null;
        try {
            ttsConfigs = TTSConfigReader.getTts();
        } catch (ThingException e) {
            return new ResponseEntity<TtsConfigs>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<TtsConfigs>(ttsConfigs, HttpStatus.OK);
    }

    @RequestMapping(value = "/Thing/TTS/speak", method = RequestMethod.POST)
    public ResponseEntity<String> speak(@RequestBody String text) {
        TtsConfig ttsConfig = null;
        try {
            ttsConfig = TTSConfigReader.getTts().getEnabledTtsConfig();
        } catch (ThingException e) {
            return new ResponseEntity<String>("No TTS configuration found", HttpStatus.BAD_REQUEST);
        }
        if (ttsConfig == null) {
            return new ResponseEntity<String>("No enabled TTS system found", HttpStatus.BAD_REQUEST);
        }
        try {
            final Boolean speak = TTSControllerFactory.getTTSController().speak(ttsConfig, text);
            if (speak != null && speak == true) {
                return new ResponseEntity<String>("Text spoken: " + text, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Failed to speak text", HttpStatus.BAD_REQUEST);
            }
        } catch (ThingException e) {
            return new ResponseEntity<String>("Failed to speak provided text", HttpStatus.BAD_REQUEST);
        }
    }

}
