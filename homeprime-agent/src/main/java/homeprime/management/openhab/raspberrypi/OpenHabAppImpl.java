package homeprime.management.openhab.raspberrypi;

import homeprime.agent.config.pojos.Management;
import homeprime.core.exception.ThingException;
import homeprime.management.ManagementApp;

/**
 *
 * Implementation of OpenHAB management application over Raspberry PI.
 *
 * @author Milan Ramljak
 *
 */
public class OpenHabAppImpl implements ManagementApp {

    private static OpenHabAppImpl singleton = null;
    private Management management = null;
    private OpenHabPush pushRunnable = null;

    @Override
    public Boolean activate() throws ThingException {
        if (getManagement() != null) {
            switch (getManagement().getManagementMode()) {
                case Push:
                    // push management mode implies that it is agent who pushed data to consumer (server)
                    // check if push thread is already active
                    if (pushRunnable == null) {
                        pushRunnable = new OpenHabPush(getManagement());
                        final Thread pushThread = new Thread(pushRunnable);
                        pushThread.start();
                        return pushThread.isAlive();
                    } else {
                        return false;
                    }
                default:
                    // other supported management modes are pull based implying it is consumer who queries agent for
                    // data
                    return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Boolean isActive() throws ThingException {
        if (getManagement() != null) {
            return pushRunnable != null && pushRunnable.isRunning();
        } else {
            return false;
        }
    }

    @Override
    public Boolean deactivate() throws ThingException {
        if (pushRunnable != null && pushRunnable.isRunning()) {
            pushRunnable.doStop();
        }
        pushRunnable = null;
        singleton = null;
        return true;
    }

    @Override
    public Boolean sync() throws ThingException {
        return OpenHabPush.sync(management);
    }

    /**
     * @return the management
     */
    public Management getManagement() {
        return management;
    }

    /**
     * @param management the management to set
     */
    public void setManagement(Management management) {
        this.management = management;
    }

    public static OpenHabAppImpl getInstance(Management management) throws ThingException {
        if (singleton == null) {
            singleton = new OpenHabAppImpl();
            singleton.setManagement(management);
        }
        return singleton;
    }

}
