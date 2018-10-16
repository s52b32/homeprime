package homeprime.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import homeprime.core.exceptions.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.properties.ThingProperties;
import homeprime.items.relay.RelayChannelStateControllerFactory;

/**
 * Main class to start STRAWA HAB RESTful service.
 * 
 * @author Milan Ramljak
 * 
 */
@SpringBootApplication
public class IoTMain {

    public static void main(String[] args) {
	SpringApplication.run(IoTMain.class, args);
	IoTLogger.getInstance().info("Initializing properties ...");
	ThingProperties.getInstance();
	IoTLogger.getInstance().info("---- STRAWA iHOUSE HAB ----");

	// Initialize relay related pins
	try {
	    IoTLogger.getInstance().info("Initializing relay pins ...");
	    RelayChannelStateControllerFactory.getRelayChannelStateReader().initialize();
	} catch (ThingException e) {
	    IoTLogger.getInstance().error("Failed to initialize relay related pins.");
	}
    }
}
