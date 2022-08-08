package homeprime.manager.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
        // show HomePrime banner
        printBanner();
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

    /**
     * Helper method to display HomePrime banner on application startup.
     */
    private static void printBanner() {
        ClassLoader classLoader = StartupManager.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("homeprime-manager-banner.txt");
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
