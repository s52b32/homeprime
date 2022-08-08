package homeprime.management.mqtt;

import homeprime.agent.config.pojos.Management;
import homeprime.core.exception.ThingException;
import homeprime.management.ManagementApp;

public class MqttAppImpl implements ManagementApp {

    private static MqttAppImpl singleton = null;
    private Management management = null;

    @Override
    public Boolean activate() throws ThingException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean isActive() throws ThingException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean deactivate() throws ThingException {
        // TODO Auto-generated method stub
        return null;
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

    public static MqttAppImpl getInstance(Management management) throws ThingException {
        if (singleton == null) {
            singleton = new MqttAppImpl();
            singleton.setManagement(management);
        }
        return singleton;
    }

    @Override
    public Boolean sync() throws ThingException {
        // TODO Auto-generated method stub
        return null;
    }

}
