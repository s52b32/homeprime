package homeprime.items.sound.raspberrypi;

import homeprime.core.commander.CmdResponse;
import homeprime.core.commander.LocalCmdExecution;
import homeprime.core.commander.LocalCmdExecutionFactory;
import homeprime.core.exception.ThingException;
import homeprime.items.sound.TTSController;
import homeprime.items.sound.tts.config.pojos.TtsConfig;

/**
 * Implementation of TTS for Raspberry PI.
 *
 * @author Milan Ramljak
 */
public class TTSControllerImpl implements TTSController {

    private static TTSControllerImpl singleton = null;

    public static TTSControllerImpl getInstance() throws ThingException {
        if (singleton == null) {
            singleton = new TTSControllerImpl();
        }
        return singleton;
    }

    @Override
    public Boolean speak(TtsConfig ttsConfig, String text) throws ThingException {
        String alteredConfig = text;
        if (text != null) {
            if (text.contains("'")) {
                alteredConfig = text.replaceAll("'", "");
            }
            if (text.contains("\"")) {
                alteredConfig = text.replaceAll("\"", "");
            }
        } else {
            throw new ThingException("Cannot speak text if value is null");
        }
        Boolean spokenSuccess = false;
        LocalCmdExecution localCmdExecution = LocalCmdExecutionFactory.getLocalSession();
        final CmdResponse speak = localCmdExecution.execute(ttsConfig.getExecutor() + " \"" + alteredConfig + "\"");
        if (speak != null && speak.getExitCode() == 0) {
            spokenSuccess = true;
        }
        // send to GC
        localCmdExecution = null;
        return spokenSuccess;
    }

}
