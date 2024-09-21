package homeprime.core.model.readers.config;

import java.io.File;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.agent.config.pojos.Agent;
import homeprime.agent.config.pojos.Management;
import homeprime.agent.config.pojos.Thing;
import homeprime.agent.config.pojos.Things;
import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.properties.ThingProperties;
import homeprime.core.utils.OsUtils;
import homeprime.core.utils.ThingUtils;

/**
 * Reader of configuration file into JSON POJO object.
 *
 * @author Milan Ramljak
 *
 */
public class ConfigurationReader {

	/**
	 * File configuration.json represents settings of the application itself. This
	 * is mandatory otherwise built with default settings.
	 */
	public static final String CONFIGURATION_FILE_NAME = "configuration.json";
	/**
	 * File things.json represents settings of the things controlled by this
	 * application. Used for HomePrime Manager.
	 */
	public static final String THINGS_CONFIGURATION_FILE_NAME = "things.json";
	private static Thing thingConfigurationPojo = null;

	/**
	 * Hidden constructor.
	 */
	private ConfigurationReader() {
	}

	public static Thing getConfiguration() throws ThingException {
		try {
			if (thingConfigurationPojo == null) {
				if (ThingUtils
						.fileExists(ThingProperties.getInstance().getConfigsRootPath() + CONFIGURATION_FILE_NAME)) {
					final String thingConfigurationString = ThingUtils
							.readFile(ThingProperties.getInstance().getConfigsRootPath() + CONFIGURATION_FILE_NAME);
					final ObjectMapper mapper = new ObjectMapper();
					thingConfigurationPojo = mapper.readValue(thingConfigurationString, Thing.class);
				} else {
					thingConfigurationPojo = buildDefaultConfiguration();
					storeDefaultConfiguration(thingConfigurationPojo);
				}
			}
			return thingConfigurationPojo;
		} catch (Exception e) {
			IoTLogger.getInstance().error(e.toString());
			throw new ThingException(
					"ERROR ConfigurationReader.getConfiguration() Failed to parse " + CONFIGURATION_FILE_NAME, e);
		}
	}

	/**
	 * Get list of all the things configuration. This is used by HomePrime Manager
	 * application.
	 *
	 * @return
	 * @throws ThingException
	 */
	public static Things getThingConfigurations() throws ThingException {
		try {
			if (ThingUtils
					.fileExists(ThingProperties.getInstance().getConfigsRootPath() + THINGS_CONFIGURATION_FILE_NAME)) {
				final String thingConfigurationsString = ThingUtils
						.readFile(ThingProperties.getInstance().getConfigsRootPath() + THINGS_CONFIGURATION_FILE_NAME);
				final ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(thingConfigurationsString, Things.class);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new ThingException("ERROR ConfigurationReader.getThingConfigurations() Failed to parse "
					+ THINGS_CONFIGURATION_FILE_NAME, e);
		}
	}

	/**
	 * Force that next getThingInfo method call reads configuration again.
	 */
	public static void reloadConfig() {
		thingConfigurationPojo = null;
	}

	/**
	 * Build configuration file with default values.
	 *
	 * @return {@link Thing}
	 */
	private static Thing buildDefaultConfiguration() {
		final Thing thingConfiguration = new Thing();
		final Agent agent = new Agent();
		final Management management = new Management();
		thingConfiguration.setName(OsUtils.getHostName());
		thingConfiguration.setUuid(UUID.randomUUID().toString());
		thingConfiguration.setAgent(agent);
		thingConfiguration.setManagement(management);
		return thingConfiguration;
	}

	/**
	 * Store thing configuration to default location.
	 * 
	 * @param thingConfiguration
	 */
	private static void storeDefaultConfiguration(final Thing thingConfiguration) {
		// check if configuration directory structure exists. Create if doesn't
		ThingUtils.checkCreateDirs(ThingProperties.getInstance().getConfigsRootPath());
		// save the file if it doesn't exist
		if (!ThingUtils.fileExists(ThingProperties.getInstance().getConfigsRootPath() + CONFIGURATION_FILE_NAME)) {
			System.out.println(ThingProperties.getInstance().getConfigsRootPath());
			try {
				final ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(
						new File(ThingProperties.getInstance().getConfigsRootPath() + CONFIGURATION_FILE_NAME),
						thingConfiguration);
			} catch (Exception e) {
				IoTLogger.getInstance().error(
						"ThingConfiguration.buildDefaultConfiguration() Failed to create " + CONFIGURATION_FILE_NAME);
				System.exit(1);
			}
		}
	}

}
