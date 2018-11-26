package homeprime.items.sound.raspberrypi;

import homeprime.core.commander.LocalCmdExecution;
import homeprime.core.commander.LocalCmdExecutionFactory;
import homeprime.core.exceptions.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.items.sound.SoundController;

/**
 * Implementation of sound control for Raspberry PI.
 * 
 * @author Milan Ramljak
 */
public class SoundControllerImpl implements SoundController {

	private static SoundControllerImpl singleton = null;

	public static SoundControllerImpl getInstance() throws ThingException {
		if (singleton == null) {
			singleton = new SoundControllerImpl();
		}
		return singleton;
	}

	@Override
	public Integer readVolume() throws ThingException {
		Integer volumeValue = null;
		LocalCmdExecution localCmdExecution = LocalCmdExecutionFactory.getLocalSession();
		String volumeRead = localCmdExecution.execute("volume && echo TRUE || echo FALSE");
		if (volumeRead != null && volumeRead.contains("TRUE")) {
			volumeRead = volumeRead.replace("TRUE", "").trim();
			try {
				volumeValue = Integer.parseInt(volumeRead);
			} catch (RuntimeException e) {
				IoTLogger.getInstance().error("Failed to parse volume value from response: " + volumeRead);
			}
		}
		return volumeValue;
	}

	@Override
	public Boolean setVolume(Integer volume) throws ThingException {
		if (volume == null) {
			throw new ThingException("Cannot set volume if value is null.");
		} else if (volume < 0 || volume > 100) {
			throw new ThingException("Invalid volume range. Possible only range from 0 to 100");
		}
		Boolean setVolumeSuccess = false;
		LocalCmdExecution localCmdExecution = LocalCmdExecutionFactory.getLocalSession();
		String volumeSet = localCmdExecution.execute("volume " + volume + " && echo TRUE || echo FALSE");
		if (volumeSet != null && volumeSet.contains("TRUE")) {
			setVolumeSuccess = true;
		}
		return setVolumeSuccess;
	}

	@Override
	public Boolean speak(String text) throws ThingException {
		if (text != null) {
			if (text.contains("'")) {
				text = text.replaceAll("'", "");
			}
			if (text.contains("\"")) {
				text = text.replaceAll("\"", "");
			}
		} else {
			throw new ThingException("Cannot speak text if value is null");
		}
		Boolean spokenSuccess = false;
		LocalCmdExecution localCmdExecution = LocalCmdExecutionFactory.getLocalSession();
		String speak = localCmdExecution.execute("speak \"" + text + "\" && echo TRUE || echo FALSE");
		if (speak != null && speak.contains("TRUE")) {
			spokenSuccess = true;
		}
		return spokenSuccess;
	}

}
