package homeprime.management;

import homeprime.core.exception.ThingException;

/**
 * Interface for management app handling.
 *
 * @author Milan Ramljak
 *
 */
public interface ManagementApp {

    /**
     * Activate management application procedures based on management mode.
     *
     * @return {@code true} if activated successfully otherwise {@code false}
     * @throws ThingException in case of issue with activation
     */
    Boolean activate() throws ThingException;

    /**
     * Check if management app is activated.
     *
     * @return {@code true} if activated otherwise {@code false}
     * @throws ThingException in case of issue with checking status
     */
    Boolean isActive() throws ThingException;

    /**
     * Deactivate management application procedures based on management mode.
     *
     * @return {@code true} if deactivated successfully otherwise {@code false}
     * @throws ThingException in case of issue with deactivation
     */
    Boolean deactivate() throws ThingException;

    /**
     * Sync agent item state with management app.
     *
     * @return {@code true} if sync went fine otherwise {@code false}
     * @throws ThingException in case of issue with synchronization
     */
    Boolean sync() throws ThingException;

}
