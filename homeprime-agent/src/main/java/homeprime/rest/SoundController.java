package homeprime.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.exceptions.ThingException;
import homeprime.items.sound.SoundControllerFactory;

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
			return new ResponseEntity<String>("Failed to read volume", HttpStatus.BAD_REQUEST);
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

	@RequestMapping(value = "/Thing/Sound/speak", method = RequestMethod.POST)
	public ResponseEntity<String> speak(@RequestBody String text) {
		try {
			final Boolean speak = SoundControllerFactory.getSoundController().speak(text);
			if (speak != null && speak == true) {
				return new ResponseEntity<String>("Text spoken: " + text, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Failed to speak text", HttpStatus.BAD_REQUEST);
			}
		} catch (ThingException e) {
			return new ResponseEntity<String>("Failed to set volume", HttpStatus.BAD_REQUEST);
		}
	}

}
