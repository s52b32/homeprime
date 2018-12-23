package homeprime.core.properties;

import java.io.File;
import java.io.IOException;

import homeprime.core.logger.IoTLogger;
import homeprime.core.system.config.enums.ThingSystemType;
import homeprime.core.utils.ThingUtils;

/**
 * Properties holder for HomePrime agent integration.
 * 
 * @author Milan Ramljak
 */
public class ThingProperties {

	private static ThingSystemType thingSystemType = ThingSystemType.RaspberryPi;
	private static ThingProperties instance;
	private static String agentConfigs = "configs/agent/";
	private static String thingConfigPath = "configs/";
	private static String uuid = null;
	private static String version = "R2A01";
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
			thingSystemType = ThingUtils.detectThingSystemType();
		}
		return instance;
	}

	/**
	 * Check if file exists and is not directory.
	 * 
	 * @param filePath full path to file
	 * @return {@code true} if file exists, otherwise {@code false}
	 */
	public String getThingUuid() {
		if (uuid != null) {
			return uuid;
		}
		final File f = new File(thingConfigPath + "thing.uuid");
		if (f.exists() && !f.isDirectory()) {
			String uuid = null;
			try {
				uuid = ThingUtils.readFile(thingConfigPath + "thing.uuid");
			} catch (IOException e) {
				IoTLogger.getInstance().error("ThingProperties.getThingUuid() Failed to read thing UUID from file");
			}
			if (uuid != null) {
				return uuid.trim();
			} else {
				IoTLogger.getInstance().info(
						"ThingProperties.getThingUuid() Thing UUID definition file shouln't be empty, proceeding with creation ...");
				ThingProperties.uuid = ThingUtils.generateThingUuid();
			}
		} else {
			IoTLogger.getInstance()
					.info("ThingProperties.getThingUuid() Thing UUID doesn't exist, proceeding with creation ...");
			ThingProperties.uuid = ThingUtils.generateThingUuid();
		}
		return uuid;
	}

	/**
	 * @return thing system type detected on running machine
	 */
	public ThingSystemType getThingSystemType() {
		return thingSystemType;
	}

	public String getThingConfigPath() {
		return thingConfigPath;
	}

	public String getAgentConfigPath() {
		return agentConfigs;
	}

	public String getThingVersion() {
		return version;
	}

	public Boolean getMaintenanceState() {
		return maintenanceState;
	}

	public void setMaintenanceState(Boolean maintenanceState) {
		ThingProperties.maintenanceState = maintenanceState;
	}

}
