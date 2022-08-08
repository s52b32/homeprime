package homeprime.management;

import homeprime.agent.config.enums.ManagementAppType;
import homeprime.agent.config.pojos.Management;
import homeprime.core.exception.ThingException;
import homeprime.core.properties.ThingProperties;
import homeprime.management.openhab.raspberrypi.OpenHabAppImpl;

/**
 * Thing management application factory.
 *
 * @author Milan Ramljak
 */
public class ManagementAppFactory {

    /**
     * Hidden constructor.
     */
    private ManagementAppFactory() {
    }

    public static ManagementApp getManagementApp(Management management) throws ThingException {
        return getManagementAppBySystemType(management);
    }

    private static ManagementApp getManagementAppBySystemType(Management management) throws ThingException {
        if (management.getAppType() == ManagementAppType.OpenHAB) {
            switch (ThingProperties.getInstance().getThingSystemType()) {
                case RaspberryPi:
                    return OpenHabAppImpl.getInstance(management);
                case Mock:
                    return homeprime.management.openhab.mock.OpenHabAppImpl.getInstance(management);
                default:
                    throw new ThingException("Not supported management app for selected system type");
            }
        } else if (management.getAppType() == ManagementAppType.MQTT) {
            switch (ThingProperties.getInstance().getThingSystemType()) {
                case RaspberryPi:
                    return OpenHabAppImpl.getInstance(management);
                case Mock:
                    return homeprime.management.openhab.mock.OpenHabAppImpl.getInstance(management);
                default:
                    throw new ThingException("Not supported management app for selected system type");
            }
        }
        throw new ThingException("Not supported management app for selected system type");
    }

}
