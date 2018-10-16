package homeprime.core.properties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import homeprime.core.logger.IoTLogger;
import homeprime.core.system.config.enums.ThingSystemType;
import homeprime.core.utils.ThingUtils;

/**
 * Properties holder for homeprime agent integration.
 * 
 * @author Milan Ramljak
 */
public class ThingProperties {

	private static ThingSystemType thingSystemType = ThingSystemType.RaspberryPi;
	private static ThingProperties instance;
	private static String agentConfigs = "configs/agent/";
	private static String thingConfigPath = "configs/";
	private static String uuid = null;
	private static String version = "R1A12";

	/**
	 * Hidden Constructor
	 */
	private ThingProperties() {
		// Hidden Constructor
	}

	public static ThingProperties getInstance() {
		if (instance == null) {
			instance = new ThingProperties();
			detectThingSystemType();
		}
		return instance;
	}

	/**
	 * Check if file exists and is not directory.
	 * 
	 * @param filePath full path to file
	 * @return {@code true} if file exists, otherwise {@code false}
	 */
	public String getUUID() {
		if (uuid != null) {
			return uuid;
		}
		final File f = new File(thingConfigPath + "agent.uuid");
		if (f.exists() && !f.isDirectory()) {
			String uuid = null;
			try {
				uuid = ThingUtils.readFile(thingConfigPath + "agent.uuid");
			} catch (IOException e) {
				IoTLogger.getInstance().error("ThingProperties.getUUID() Failed to read thing UUID from file");
			}
			if (uuid != null) {
				return uuid.trim();
			} else {
				IoTLogger.getInstance().info(
						"ThingProperties.getUUID() Thing UUID definition file shouln't be empty, proceeding with creation ...");
				return createUUID();
			}
		} else {
			IoTLogger.getInstance()
					.info("ThingProperties.getUUID() Thing UUID doesn't exist, proceeding with creation ...");
			return createUUID();
		}
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

	/**
	 * Helper method for thing system type detection. Uses local session to execute
	 * {@code uname -n} command
	 */
	private static void detectThingSystemType() {
		String nodename = null;
		try {
			nodename = ThingUtils.readFile(thingConfigPath + "thing.info");
			if (nodename == null) {
				IoTLogger.getInstance()
						.info("ERROR ThingProperties.detectThingSystemType() Failed to get thing system type from "
								+ thingConfigPath + "thing.info");
				thingSystemType = ThingSystemType.Unknown;
			} else if (nodename.contains("raspberrypi")) {
				thingSystemType = ThingSystemType.RaspberryPi;
			} else if (nodename.contains("bannanapi")) {
				thingSystemType = ThingSystemType.BananaPi;
			} else if (nodename.contains("beagleboneblack")) {
				thingSystemType = ThingSystemType.BeagleBoneBlack;
			} else if (nodename.contains("mock")) {
				thingSystemType = ThingSystemType.Mock;
			} else {
				IoTLogger.getInstance().info(
						"ERROR ThingProperties.detectThingSystemType() Detect system type returned unknown value: "
								+ nodename);
				thingSystemType = ThingSystemType.Unknown;
			}
		} catch (IOException e) {
			IoTLogger.getInstance().info(
					"ERROR ThingProperties.detectThingSystemType() Failed to read " + thingConfigPath + "thing.info");
			thingSystemType = ThingSystemType.Unknown;
		}
	}

	/**
	 * Helper method which creates UUID and writes it to {@code configs/thing.uuid}
	 * file.
	 * 
	 * @return uuid as string
	 */
	private String createUUID() {
		final Path file = Paths.get(thingConfigPath + "thing.uuid");
		final UUID generatedId = UUID.randomUUID();
		try {
			Files.write(file, generatedId.toString().getBytes());
		} catch (IOException e) {
			IoTLogger.getInstance().info(
					"ERROR ThingProperties.createUUID() Failed to write UUID to: " + thingConfigPath + "thing.uuid");
			System.exit(1);
		}
		uuid = generatedId.toString();
		return uuid;
	}

}
