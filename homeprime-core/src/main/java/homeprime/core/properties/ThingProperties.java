package homeprime.core.properties;

import homeprime.agent.config.enums.SystemType;
import homeprime.core.enums.AppMode;
import homeprime.core.utils.ThingUtils;

/**
 * Properties holder for HomePrime agent integration.
 *
 * @author Milan Ramljak
 */
public class ThingProperties {

    // default application type is agent
    private static AppMode APP_MODE = AppMode.Agent;
    // default agent rest port is 8081
    public static int DEFAULT_REST_PORT = 8081;
    // default agent rest port is 8081
    public static int DEFAULT_REST_PORT_MANAGER = 8082;
    // relative path (default) where images are stored
    public static final String APP_ROOT_PATH = "/usr/local/HomePrime/";
    public static final String APP_ROOT_PATH_MANAGER = "/usr/local/HomePrimeManager/";
    private static SystemType thingSystemType = SystemType.RaspberryPi;
    private static ThingProperties instance;
    // service name of agent
    public static final String SERVICE_NAME = "homeprime";
    // service name of manager
    public static final String SERVICE_NAME_MANAGER = "homeprime-manager";

    /**
     * Initial state of maintenance is set to disabled (false). Disabled once system
     * starts.
     */
    private static Boolean maintenanceState = false;

    /**
     * Hidden Constructor
     */
    private ThingProperties() {
        // Hidden Constructor
    }

    public static ThingProperties getInstance() {
        if (instance == null) {
            instance = new ThingProperties();
        }
        return instance;
    }

    /**
     * @return thing system type detected on running machine
     */
    public SystemType getThingSystemType() {
        thingSystemType = ThingUtils.detectThingSystemType();
        return thingSystemType;
    }

    public Boolean getMaintenanceState() {
        return maintenanceState;
    }

    public void setMaintenanceState(Boolean maintenanceState) {
        ThingProperties.maintenanceState = maintenanceState;
    }

    public AppMode getAppMode() {
        return APP_MODE;
    }

    public void setAppMode(AppMode appMode) {
        ThingProperties.APP_MODE = appMode;
    }

    public String getAppRootPath() {
        if (ThingProperties.APP_MODE == AppMode.Manager) {
            return APP_ROOT_PATH_MANAGER;
        }
        return APP_ROOT_PATH;
    }

    public String getConfigsRootPath() {
        return getAppRootPath() + "configs/";
    }

    public String getBackupsRootPath() {
        return getAppRootPath() + "backups/";
    }

    public String getItemsRootPath() {
        return getConfigsRootPath() + "items/";
    }

    public String getSslRootPath() {
        return getConfigsRootPath() + "ssl/";
    }

    public int getDefaultRestPort() {
        if (ThingProperties.APP_MODE == AppMode.Manager) {
            return DEFAULT_REST_PORT_MANAGER;
        }
        return DEFAULT_REST_PORT;
    }

}
