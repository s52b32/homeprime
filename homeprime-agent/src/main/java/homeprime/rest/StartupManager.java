package homeprime.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.ConfigurationValidator;
import homeprime.core.properties.ThingProperties;
import homeprime.items.contact.ContactSensorControllerFactory;

/**
 * Main class to start HomePrime Agent RESTful service.
 *
 * @author Milan Ramljak
 *
 */
@SpringBootApplication
public class StartupManager {

    public static void main(String[] args) {
        // show HomePrime banner
        printBanner();
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
        // check what management type agent is
        IoTLogger.getInstance().info("---- HomePrime Agent (started) ----");
        // configure contact pull resistance
        try {
            ContactSensorControllerFactory.getContactSensorsReader();
        } catch (ThingException e) {
            IoTLogger.getInstance().error("Failed to configure initial contact pull resistance!");
        }
    }

    /**
     * Helper method to display HomePrime banner on application startup.
     */
    private static void printBanner() {
        ClassLoader classLoader = StartupManager.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("homeprime-banner.txt");
        if (inputStream != null) {
            try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(streamReader)) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

            } catch (IOException e) {
                IoTLogger.getInstance().error("Failed to display HomePrime banner!");
            }
        }

    }
}
