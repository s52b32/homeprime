package homeprime.items.sound;

import homeprime.core.exception.ThingException;

/**
 * Interface for streaming operations.
 *
 * @author Milan Ramljak
 *
 */
public interface StreamingController {

    /**
     * Start stream play..
     *
     * @return {@code true} if successful
     * @throws ThingException in case of error while playing stream
     */
    Boolean play(Integer streamId) throws ThingException;

    /**
     * Stop streaming play.
     *
     * @return {@code true} if successful
     * @throws ThingException in case of error while stopping stream
     */
    Boolean stop() throws ThingException;

    /**
     * Is stream playing.
     *
     * @return {@code true} if successful
     * @throws ThingException
     */
    Boolean isPlaying() throws ThingException;

    /**
     *
     * @return id of active (playing) stream id
     * @throws ThingException
     */
    Integer getActiveStreamId() throws ThingException;
}
