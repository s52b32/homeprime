package homeprime.items.sound.raspberrypi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import homeprime.core.commander.CmdResponse;
import homeprime.core.commander.LocalCmdExecution;
import homeprime.core.commander.LocalCmdExecutionFactory;
import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.readers.items.sound.SoundConfigReader;
import homeprime.items.sound.SoundController;
import homeprime.items.sound.config.pojos.Sound;
import homeprime.items.sound.config.pojos.Speaker;

/**
 * Implementation of sound control for Raspberry PI.
 *
 * @author Milan Ramljak
 */
public class SoundControllerImpl implements SoundController {

    private static SoundControllerImpl singleton = null;

    /**
     * Singleton. Only one instance can exist.
     *
     * @return implementation object
     * @throws ThingException
     */
    public static SoundControllerImpl getInstance() throws ThingException {
        if (singleton == null) {
            singleton = new SoundControllerImpl();
        }
        return singleton;
    }

    @Override
    public Integer readVolume() throws ThingException {
        Integer volumeValue = null;
        Speaker activeSpeaker = getActiveSpeaker();
        if (activeSpeaker == null) {
            IoTLogger.getInstance().error("Cannot read volume level if not active speakers exist!");
        } else {
            LocalCmdExecution localCmdExecution = LocalCmdExecutionFactory.getLocalSession();
            final CmdResponse volumeRead = localCmdExecution.execute("amixer get " + activeSpeaker.getChannelId());
            if (volumeRead != null && volumeRead.getExitCode() == 0) {
                final String volumePercentagePattern = "(\\d+%)";
                final Pattern pattern = Pattern.compile(volumePercentagePattern);

                try {
                    final Matcher matcher = pattern.matcher(volumeRead.getResponse());
                    if (matcher.find()) {
                        // 21%
                        volumeValue = Integer.parseInt(matcher.group().replace("%", ""));
                    } else {
                        IoTLogger.getInstance()
                                .error("Failed to match volume value pattern from response: " + volumeRead);
                    }
                } catch (RuntimeException e) {
                    IoTLogger.getInstance().error("Failed to parse volume value from response: " + volumeRead);
                }
            }
            // send to GC
            localCmdExecution = null;
        }
        return volumeValue;
    }

    @Override
    public Boolean setVolume(Integer volume) throws ThingException {
        Boolean setVolumeSuccess = null;
        if (volume == null) {
            throw new ThingException("Cannot set volume if value is null.");
        } else if (volume < 0 || volume > 100) {
            throw new ThingException("Invalid volume range. Possible only range from 0 to 100");
        }
        Speaker activeSpeaker = getActiveSpeaker();
        if (activeSpeaker == null) {
            IoTLogger.getInstance().error("Cannot read volume level if not active speakers exist!");
        } else {
            setVolumeSuccess = false;
            LocalCmdExecution localCmdExecution = LocalCmdExecutionFactory.getLocalSession();
            final CmdResponse volumeSet = localCmdExecution
                    .execute("amixer set " + activeSpeaker.getChannelId() + " " + volume + "%");
            if (volumeSet != null && volumeSet.getExitCode() == 0) {
                setVolumeSuccess = true;
            }
            // send to GC
            localCmdExecution = null;
        }
        return setVolumeSuccess;
    }

    @Override
    public StreamingControllerImpl stream() throws ThingException {
        return StreamingControllerImpl.getInstance();
    }

    /**
     * Helper method to get active speaker object.
     *
     * @return JSON object of active speaker
     * @throws ThingException in case of failures reading sound configuration JSON file.
     */
    private Speaker getActiveSpeaker() throws ThingException {
        final Sound sound = SoundConfigReader.getSound();
        if (sound != null) {
            for (Speaker speaker : sound.getSpeakers()) {
                if (speaker.getIsActive()) {
                    return speaker;
                }
            }
        }
        return null;
    }

}
