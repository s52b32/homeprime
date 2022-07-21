package homeprime.items.sound.raspberrypi;

import homeprime.core.commander.CmdResponse;
import homeprime.core.commander.LocalCmdExecutionFactory;
import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;

/**
 * Thread for streaming service.
 *
 * @author Milan Ramljak
 */
public class StreamingService extends Thread {
    private String streamUrl = null;
    private boolean isStreaming = false;

    public StreamingService(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    @Override
    public void run() {
        if (streamUrl == null) {
            IoTLogger.getInstance().error("Cannot stream if stream url in null");
        }
        try {
            // TODO: improve. i.e. create wrapper around mplayer as one implementation
            final CmdResponse stream = LocalCmdExecutionFactory.getLocalSession().execute(
                    "nohup mplayer -cache 384 -msglevel cache=-1:identify=3:statusline=-1 -ao alsa:noblock -playlist "
                            + streamUrl);
            if (stream != null && stream.getExitCode() == 0) {
                IoTLogger.getInstance().info("Streaming succesfully started: " + streamUrl);
                // at this stage consider streaming to be running fine
                setStreamingStatus(true);
                IoTLogger.getInstance().error(stream.getResponse());
            } else {
                try {
                    IoTLogger.getInstance().error("Failed to get success response while starting streaming!");
                } catch (NullPointerException e) {
                    // in case of interruption of command execution
                }
                setStreamingStatus(false);
            }
        } catch (ThingException e) {
            IoTLogger.getInstance().error("Failed to execute stream from: " + streamUrl);
            // at this stage consider streaming to be stopped
            setStreamingStatus(false);
        }
    }

    /**
     * @return the isStreaming
     */
    public boolean isStreaming() {
        return isStreaming;
    }

    /**
     * Get stream url used to stream sound from.
     *
     * @return stream url
     */
    public String getStreamUrl() {
        return streamUrl;
    }

    /**
     * Stop streaming by killing all of the mplayer processes on machine.
     */
    public void stopStreaming() {
        try {
            // kill all mplayer processes
            CmdResponse response = LocalCmdExecutionFactory.getLocalSession().execute("pkill mplayer");
            IoTLogger.getInstance().error(response.getResponse());
            setStreamingStatus(false);
        } catch (ThingException e) {
            IoTLogger.getInstance().error("Failed to stop streaming service: " + streamUrl);
        }
    }

    /**
     * @param isStreaming the isStreaming to set
     */
    private void setStreamingStatus(boolean isStreaming) {
        this.isStreaming = isStreaming;
    }
}
