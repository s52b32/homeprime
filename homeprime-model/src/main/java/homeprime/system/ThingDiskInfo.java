package homeprime.system;

import homeprime.core.exception.ThingException;

/**
 * Interface describing thing disk space information.
 *
 * @author Milan Ramljak
 *
 */
public interface ThingDiskInfo {

    /**
     * Get total disk space of running machine.
     *
     * @return total disk space.
     * @throws ThingException in case of any failure
     */
    long getTotalDiskSpace() throws ThingException;

    /**
     * Get used disk space of running machine.
     *
     * @return used disk space.
     * @throws ThingException in case of any failure
     */
    long getUsedDiskSpace() throws ThingException;

    /**
     * Get free disk space of running machine.
     *
     * @return free disk space.
     * @throws ThingException in case of any failure
     */
    long getFreeDiskSpace() throws ThingException;

}
