package homeprime.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import homeprime.core.exceptions.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.properties.ThingProperties;
import homeprime.items.relay.RelayStateControllerFactory;

/**
 * Main class to start HomePrime Agent RESTful service.
 * 
 * @author Milan Ramljak
 * 
 */
@SpringBootApplication
public class StartupManager {

	public static void main(String[] args) {
		IoTLogger.getInstance().info("---- HomePrime Agent (starting)----");
		SpringApplication.run(StartupManager.class, args);
		IoTLogger.getInstance().info("Initializing properties ...");
		ThingProperties.getInstance();
		IoTLogger.getInstance().info("---- HomePrime Agent (started) ----");

		// Initialize relay related pins
		try {
			IoTLogger.getInstance().info("Initializing relay pins ...");
			RelayStateControllerFactory.getRelayStateReader().initialize();
		} catch (ThingException e) {
			IoTLogger.getInstance().error("Failed to initialize relay related pins.");
		}
	}
}
