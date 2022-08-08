package homeprime.rest;

import java.io.File;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.readers.items.sound.SoundConfigReader;
import homeprime.core.properties.ThingProperties;
import homeprime.core.utils.ThingUtils;
import homeprime.items.sound.SoundControllerFactory;
import homeprime.items.sound.config.pojos.Sound;
import homeprime.items.sound.config.pojos.Stream;

/**
 * Spring REST controller for thing sound control.
 *
 * @author Milan Ramljak
 *
 */
@RestController
public class SoundController {

    @RequestMapping("/Thing/Sound/Volume/read")
    public ResponseEntity<String> getVolume() {
        try {
            final Integer readVolume = SoundControllerFactory.getSoundController().readVolume();
            if (readVolume != null) {
                return new ResponseEntity<String>(readVolume.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Failed to read volume", HttpStatus.BAD_REQUEST);
            }
        } catch (ThingException e) {
            return new ResponseEntity<String>("Exc Failed to read volume", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/Thing/Sound/Volume/set-{volume}")
    public ResponseEntity<String> setVolume(@PathVariable(value = "volume") int volume) {
        try {
            final Boolean setVolume = SoundControllerFactory.getSoundController().setVolume(volume);
            if (setVolume != null && setVolume == true) {
                return new ResponseEntity<String>("Volume set to: " + volume, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Failed to set volume", HttpStatus.BAD_REQUEST);
            }
        } catch (ThingException e) {
            return new ResponseEntity<String>("Failed to set volume", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/Thing/Sound", method = RequestMethod.GET)
    public ResponseEntity<Sound> getSoundOverview() {
        Sound sound = null;
        try {
            sound = SoundConfigReader.getSound();
        } catch (ThingException e) {
            return new ResponseEntity<Sound>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Sound>(sound, HttpStatus.OK);
    }

    @RequestMapping(value = "/Thing/Sound/Stream", method = RequestMethod.GET)
    public ResponseEntity<List<Stream>> getStreamOverview() {
        List<Stream> streams = null;
        try {
            final Sound sound = SoundConfigReader.getSound();
            if (sound != null) {
                final List<Stream> streamList = sound.getStreams();
                final Integer activeStreamId = SoundControllerFactory.getSoundController().stream().getActiveStreamId();
                if (!streamList.isEmpty()) {
                    for (Stream stream : streamList) {
                        if (activeStreamId != null && stream.getId() == activeStreamId) {
                            stream.setIsActive(true);
                        } else {
                            stream.setIsActive(false);
                        }
                    }
                    streams = streamList;
                }
            }
        } catch (ThingException e) {
            return new ResponseEntity<List<Stream>>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<List<Stream>>(streams, HttpStatus.OK);
    }

    /**
     * REST endpoint to update thing sound configuration in configuration file.
     *
     * @param sound sound JSON object
     * @return 202 ACCEPTED for success or 409 CONFLICT for failed try
     */
    @PostMapping("/Thing/Sound/update")
    public ResponseEntity<?> updateSound(@RequestPart(required = true) Sound sound) {
        boolean updateSoundConfiguration = updateSoundConfiguration(sound);
        if (updateSoundConfiguration) {
            return new ResponseEntity<String>("Thing sound configuration updated", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<String>("Failed to write new sound configuration", HttpStatus.CONFLICT);
    }

    /**
     * REST endpoint to update thing sound streams configuration in configuration file.
     *
     * @param streams list of stream JSON objects
     * @return 202 ACCEPTED for success or 409 CONFLICT for failed try
     */
    @PostMapping("/Thing/Sound/Stream/update")
    public ResponseEntity<?> updateSoundStreams(@RequestPart(required = true) List<Stream> streams) {
        boolean updateSoundConfiguration = updateStreamsConfiguration(streams);
        if (updateSoundConfiguration) {
            return new ResponseEntity<String>("Thing sound configuration updated", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<String>("Failed to write new sound configuration", HttpStatus.CONFLICT);
    }

    /**
     * REST endpoint to reload thing sound configuration from file.
     *
     * @return
     */
    @RequestMapping(value = "/Thing/Sound/reload-config", method = RequestMethod.GET)
    public ResponseEntity<String> syncSoundConfig() {
        SoundConfigReader.reloadConfig();
        return new ResponseEntity<String>("Thing sound config re-sync scheduled", HttpStatus.OK);
    }

    @RequestMapping(value = "/Thing/Sound/Stream/{streamId}/play")
    public ResponseEntity<String> streamPlay(@PathVariable(value = "streamId") int streamId) {
        try {
            // Streaming play initiated
            SoundControllerFactory.getSoundController().stream().play(streamId);
            if (SoundControllerFactory.getSoundController().stream().isPlaying()) {
                return new ResponseEntity<String>("Streaming started: " + streamId, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Streaming failed to start: " + streamId, HttpStatus.OK);
            }
        } catch (ThingException e) {
            return new ResponseEntity<String>("Failed to play stream", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/Thing/Sound/Stream/read")
    public ResponseEntity<String> isStreamPlaying() {
        try {
            if (SoundControllerFactory.getSoundController().stream().isPlaying()) {
                return new ResponseEntity<String>("Streaming", HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Not streaming!", HttpStatus.OK);
            }
        } catch (ThingException e) {
            return new ResponseEntity<String>("Failed to read stream status", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/Thing/Sound/Stream/stop")
    public ResponseEntity<String> streamStop() {
        try {
            // Streaming stop initiated
            SoundControllerFactory.getSoundController().stream().stop();
            if (!SoundControllerFactory.getSoundController().stream().isPlaying()) {
                return new ResponseEntity<String>("Streaming stopped", HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Streaming not stopped!", HttpStatus.OK);
            }
        } catch (ThingException e) {
            return new ResponseEntity<String>("Failed to stop stream", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Helper method to perform update of thing sound configuration and write it to file.
     *
     * @param sound sound JSON object
     * @return
     */
    private boolean updateSoundConfiguration(Sound sound) {
        // update file only if it exists
        if (ThingUtils.fileExists(
                ThingProperties.getInstance().getConfigsRootPath() + SoundConfigReader.SOUND_CONFIGURATION_FILE_NAME)) {
            // read thing configuration
            try {
                // now write updated configuration to file
                final ObjectMapper mapper = new ObjectMapper();
                // make it nicely formatted
                mapper.writerWithDefaultPrettyPrinter();
                mapper.writeValue(new File(ThingProperties.getInstance().getItemsRootPath()
                        + SoundConfigReader.SOUND_CONFIGURATION_FILE_NAME), sound);
                // at this point indicate success
                return true;
            } catch (Exception e) {
                IoTLogger.getInstance().error("Failed to write to sound configuration file "
                        + SoundConfigReader.SOUND_CONFIGURATION_FILE_NAME);
            }
        }
        return false;
    }

    /**
     * Helper method to perform update of thing sound streams configuration and write it to file.
     *
     * @param soundStreams list of stream
     * @return
     */
    private boolean updateStreamsConfiguration(List<Stream> soundStreams) {
        // update file only if it exists
        if (ThingUtils.fileExists(
                ThingProperties.getInstance().getItemsRootPath() + SoundConfigReader.SOUND_CONFIGURATION_FILE_NAME)) {
            // read thing sound configuration
            try {
                final Sound thingSoundConfiguration = SoundConfigReader.getSound();
                // update stream list section
                thingSoundConfiguration.setStreams(soundStreams);
                // now write updated configuration to file
                final ObjectMapper mapper = new ObjectMapper();
                // make it nicely formatted
                mapper.writerWithDefaultPrettyPrinter();
                mapper.writeValue(new File(ThingProperties.getInstance().getItemsRootPath()
                        + SoundConfigReader.SOUND_CONFIGURATION_FILE_NAME), thingSoundConfiguration);
                // at this point indicate success
                return true;
            } catch (ThingException e) {
                IoTLogger.getInstance().error("Failed to read agent/thing sound configuration file "
                        + SoundConfigReader.SOUND_CONFIGURATION_FILE_NAME);

            } catch (Exception e) {
                IoTLogger.getInstance().error("Failed to write to sound configuration file "
                        + SoundConfigReader.SOUND_CONFIGURATION_FILE_NAME);
            }
        }
        return false;
    }

}
