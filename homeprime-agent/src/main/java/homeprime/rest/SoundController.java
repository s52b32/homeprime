package homeprime.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.exception.ThingException;
import homeprime.core.model.readers.items.sound.SoundConfigReader;
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

}
