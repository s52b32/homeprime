package homeprime.manager.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import homeprime.core.enums.AppMode;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.ConfigurationValidator;
import homeprime.core.properties.ThingProperties;

/**
 * Main class to start HomePrime Manager RESTful service.
 *
 * @author Milan Ramljak
 *
 */
@SpringBootApplication
public class StartupManager {

    public static void main(String[] args) {
        ThingProperties.getInstance().setAppMode(AppMode.Manager);
        IoTLogger.getInstance().info("---- HomePrime Manager (starting)----");
        ConfigurationValidator.createDirectoryStructure();
        IoTLogger.getInstance().info("Validate configuration ...");
        if (!ConfigurationValidator.validate()) {
            IoTLogger.getInstance().error("Configuration is not valid!");
            IoTLogger.getInstance().info("---- HomePrime Manager (failed)----");
            /* configuration error */
            System.exit(78);
        }
        SpringApplication.run(StartupManager.class, args);
        IoTLogger.getInstance().info("---- HomePrime Manager (started) ----");
    }

}
