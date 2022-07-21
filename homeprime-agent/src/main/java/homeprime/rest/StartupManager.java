package homeprime.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import homeprime.core.logger.IoTLogger;
import homeprime.core.model.ConfigurationValidator;
import homeprime.core.properties.ThingProperties;

/**
 * Main class to start HomePrime Agent RESTful service.
 *
 * @author Milan Ramljak
 *
 */
@SpringBootApplication
public class StartupManager {

    public static void main(String[] args) {
        IoTLogger.getInstance().info("Initializing properties ...");
        ThingProperties.getInstance();
        IoTLogger.getInstance().info("---- HomePrime Agent (starting)----");
        IoTLogger.getInstance().info("Validate configuration ...");
        ConfigurationValidator.createDirectoryStructure();
        if (!ConfigurationValidator.validate()) {
            IoTLogger.getInstance().error("Configuration is not valid!");
            IoTLogger.getInstance().info("---- HomePrime Agent (failed)----");
            /* configuration error */
            System.exit(78);
        }
        SpringApplication.run(StartupManager.class, args);
        IoTLogger.getInstance().info("---- HomePrime Agent (started) ----");
    }
}
