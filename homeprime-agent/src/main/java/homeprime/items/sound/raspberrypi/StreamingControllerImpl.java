package homeprime.items.sound.raspberrypi;

import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.readers.items.sound.SoundConfigReader;
import homeprime.items.sound.StreamingController;
import homeprime.items.sound.config.pojos.Sound;
import homeprime.items.sound.config.pojos.Stream;

/**
 * Controller of streaming service.
 *
 * @author Milan Ramljak
 */
public class StreamingControllerImpl implements StreamingController {

    private static StreamingControllerImpl instance;
    private static Integer activeStreamId = null;

    /**
     * Holder for streaming executor.
     */
    private static StreamingService streaming = null;

    /**
     * Hidden Constructor
     */
    private StreamingControllerImpl() {
        // Hidden Constructor
    }

    public static StreamingControllerImpl getInstance() {
        if (instance == null) {
            instance = new StreamingControllerImpl();
        }
        return instance;
    }

    private Stream getStream(int streamId) throws ThingException {
        final Sound sound = SoundConfigReader.getSound();
        Stream stream = null;
        if (sound != null) {
            for (Stream streamE : sound.getStreams()) {
                if (streamE.getId() == streamId) {
                    return streamE;
                }
            }
        }
        return stream;
    }

    @Override
    public Integer getActiveStreamId() {
        if (isPlaying()) {
            return activeStreamId;
        }
        return null;
    }

    @Override
    public Boolean isPlaying() {
        if (streaming == null) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean play(Integer streamId) {
        Stream stream = null;
        Boolean result = null;
        try {
            stream = getStream(streamId);
        } catch (ThingException e) {
            result = false;
        }

        if (stream == null) {
            IoTLogger.getInstance().warn("Cannot play stream if ID " + streamId + " was not found in configuration!");
        } else {
            if (!isPlaying()) {
                // build service
                streaming = new StreamingService(stream.getUrl());
                // now start thread to play from stream
                streaming.start();
                result = true;
                activeStreamId = streamId;
            } else {
                IoTLogger.getInstance().warn("Streaming already started!");
            }
        }
        return result;
    }

    @Override
    public Boolean stop() {
        Boolean result = null;
        if (isPlaying()) {
            // stop streaming
            streaming.stopStreaming();
            // now stop thread holding streaming service
            streaming.interrupt();
            IoTLogger.getInstance().info("Streaming stopped");
            result = true;
            streaming = null;
            activeStreamId = null;
        } else {
            IoTLogger.getInstance().warn("Streaming already stopped!");
        }
        return result;
    }

}
