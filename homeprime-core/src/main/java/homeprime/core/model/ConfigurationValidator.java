package homeprime.core.model;

import java.io.File;

import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.readers.config.ConfigurationReader;
import homeprime.core.model.readers.items.contact.ContactsConfigReader;
import homeprime.core.model.readers.items.relay.RelayConfigReader;
import homeprime.core.model.readers.items.sound.SoundConfigReader;
import homeprime.core.model.readers.items.sound.TTSConfigReader;
import homeprime.core.model.readers.items.temperature.TemperatureConfigReader;
import homeprime.core.properties.ThingProperties;
import homeprime.core.utils.ThingUtils;

/**
 * Validate configuration files.
 *
 * @author Milan Ramljak
 *
 */
public class ConfigurationValidator {

    /**
     * Hidden constructor.
     */
    private ConfigurationValidator() {
    }

    /**
     * Helper method to validate JSON configuration data.
     *
     * @return {@code true} if successful, otherwise {@code false}
     */
    public static boolean validate() {
        boolean validationResult = true;
        // things.json file should exist only in case of HomePrime Manager
        if (ThingUtils.fileExists(ThingProperties.getInstance().getConfigsRootPath()
                + ConfigurationReader.THINGS_CONFIGURATION_FILE_NAME)) {
            // validate configuration of manager only
            try {
                ConfigurationReader.getThingConfigurations();
            } catch (ThingException e) {
                IoTLogger.getInstance()
                        .error("Provided " + ConfigurationReader.THINGS_CONFIGURATION_FILE_NAME + " is not valid!");
                validationResult = false;
            }
        } else {
            // validate configuration of agent
            try {
                ConfigurationReader.getConfiguration();
            } catch (ThingException e) {
                IoTLogger.getInstance()
                        .error("Provided " + ConfigurationReader.CONFIGURATION_FILE_NAME + " is not valid!");
                validationResult = false;
            }
            try {
                ContactsConfigReader.getContactSensors();
            } catch (ThingException e) {
                IoTLogger.getInstance()
                        .error("Provided " + ContactsConfigReader.CONTACTS_CONFIGURATION_FILE_NAME + " is not valid!");
                validationResult = false;
            }
            try {
                RelayConfigReader.getRelays();
            } catch (ThingException e) {
                IoTLogger.getInstance()
                        .error("Provided " + RelayConfigReader.RELAYS_CONFIGURATION_FILE_NAME + " is not valid!");
                validationResult = false;
            }
            try {
                TemperatureConfigReader.getTemperatureSensors();
            } catch (ThingException e) {
                IoTLogger.getInstance().error(
                        "Provided " + TemperatureConfigReader.TEMPERATURES_CONFIGURATION_FILE_NAME + " is not valid!");
                validationResult = false;
            }
            try {
                SoundConfigReader.getSound();
            } catch (ThingException e) {
                IoTLogger.getInstance()
                        .error("Provided " + SoundConfigReader.SOUND_CONFIGURATION_FILE_NAME + " is not valid!");
                validationResult = false;
            }
            try {
                TTSConfigReader.getTts();
            } catch (ThingException e) {
                IoTLogger.getInstance()
                        .error("Provided " + TTSConfigReader.TTS_CONFIGURATION_FILE_NAME + " is not valid!");
                validationResult = false;
            }
        }
        return validationResult;

    }

    /**
     * Create application directory structure if it doesn't exist. This creates configs, configs/items and configs/ssl
     */
    public static void createDirectoryStructure() {
        // create dir: configs
        final File configsRootDir = new File(ThingProperties.getInstance().getConfigsRootPath());
        if (!configsRootDir.exists()) {
            // this must exist, otherwise app cannot start. This is just in case.
            configsRootDir.mkdirs();
            configsRootDir.setReadable(true);
            configsRootDir.setWritable(true);
        }
        // create dir: configs/items
        final File configsItemsDir = new File(ThingProperties.getInstance().getItemsRootPath());
        if (!configsItemsDir.exists()) {
            configsItemsDir.mkdirs();
            configsItemsDir.setReadable(true);
            configsItemsDir.setWritable(true);
        }
        // create dir: configs/ssl
        final File configsSslDir = new File(ThingProperties.getInstance().getSslRootPath());
        if (!configsSslDir.exists()) {
            configsSslDir.mkdirs();
            configsSslDir.setReadable(true);
            configsSslDir.setWritable(true);
        }
        // create dir: configs/backups
        final File configsBackupsDir = new File(ThingProperties.getInstance().getBackupsRootPath());
        if (!configsBackupsDir.exists()) {
            configsBackupsDir.mkdirs();
            configsBackupsDir.setReadable(true);
            configsBackupsDir.setWritable(true);
        }

    }

}
