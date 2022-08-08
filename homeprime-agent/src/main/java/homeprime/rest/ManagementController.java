package homeprime.rest;

import java.io.File;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.agent.config.pojos.Management;
import homeprime.agent.config.pojos.Thing;
import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.readers.config.ConfigurationReader;
import homeprime.core.properties.ThingProperties;
import homeprime.core.utils.ThingUtils;
import homeprime.management.ManagementAppFactory;

/**
 * Spring REST controller for thing synchronization depending on management type.
 *
 * @author Milan Ramljak
 */
@RestController
public class ManagementController {

    /**
     * Get thing management settings.
     *
     * @return 200 OK
     * @throws ThingException
     */
    @GetMapping("/Thing/Management")
    public Management getManagement() throws ThingException {
        return readManagementSettings();
    }

    /**
     * REST endpoint to update management configuration in agent configuration file.
     *
     * @param management management JSON object
     * @return 202 ACCEPTED for success or 409 CONFLICT for failed try
     */
    @PostMapping("/Thing/Management/update")
    public ResponseEntity<?> updateManagement(@RequestPart(required = true) Management management) {
        boolean updateManagementConfiguration = updateManagementConfiguration(management);
        if (updateManagementConfiguration) {
            return new ResponseEntity<String>("Management configuration updated", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<String>("Failed to write new configuration", HttpStatus.CONFLICT);
    }

    /**
     * REST endpoint to reload thing configuration from file.
     *
     * @return
     */
    @RequestMapping(value = "/Thing/Management/reload-config", method = RequestMethod.GET)
    public ResponseEntity<String> syncThingConfig() {
        ConfigurationReader.reloadConfig();
        return new ResponseEntity<String>("Thing config re-sync scheduled", HttpStatus.OK);
    }

    /**
     * Activate thing management application.
     *
     * @return 200 OK
     * @throws ThingException
     */
    @GetMapping("/Thing/Management/activate")
    public ResponseEntity<?> activateManagementMode() throws ThingException {
        final Management thingManagement = readManagementSettings();
        if (thingManagement != null) {
            switch (thingManagement.getManagementMode()) {
                case Pull:
                    // consumer application uses REST API to control/read agent data.
                    return new ResponseEntity<Boolean>(true, HttpStatus.OK);
                case Push:
                    // agent pushes data towards consumer. This depends on server application type.
                    final Boolean appActivationStatus = ManagementAppFactory.getManagementApp(thingManagement)
                            .activate();
                    if (appActivationStatus != null) {
                        return new ResponseEntity<Boolean>(appActivationStatus, HttpStatus.OK);
                    }
                    return new ResponseEntity<String>("Failed to activate PUSH management type",
                            HttpStatus.BAD_REQUEST);
                default:
                    // default is Unknown. This does nothing.
                    return new ResponseEntity<String>("Unknown management type cannot be activated",
                            HttpStatus.METHOD_NOT_ALLOWED);
            }
        } else {
            return new ResponseEntity<String>("No thing mangement settings found", HttpStatus.FAILED_DEPENDENCY);
        }
    }

    /**
     * Deactivate thing management application.
     *
     * @return 200 OK
     * @throws ThingException
     */
    @GetMapping("/Thing/Management/de-activate")
    public ResponseEntity<?> deactivateManagementMode() throws ThingException {
        final Management thingManagement = readManagementSettings();
        if (thingManagement != null) {
            switch (thingManagement.getManagementMode()) {
                case Pull:
                    // consumer application uses REST API to control/read agent data.
                    return new ResponseEntity<Boolean>(true, HttpStatus.OK);
                case Push:
                    // agent pushes data towards consumer. This depends on server application type.
                    final Boolean appActivationStatus = ManagementAppFactory.getManagementApp(thingManagement)
                            .deactivate();
                    System.out.println("deact rest");
                    if (appActivationStatus != null) {
                        return new ResponseEntity<Boolean>(appActivationStatus, HttpStatus.OK);
                    }
                    return new ResponseEntity<String>("Failed to deactivate PUSH management type",
                            HttpStatus.BAD_REQUEST);
                default:
                    // default is Unknown. This does nothing.
                    return new ResponseEntity<String>("Unknown management type cannot be deactivated",
                            HttpStatus.METHOD_NOT_ALLOWED);
            }
        } else {
            return new ResponseEntity<String>("No thing mangement settings found", HttpStatus.FAILED_DEPENDENCY);
        }
    }

    /**
     * Sync agent items with management app based on management mode.
     *
     * @return 200 OK
     * @throws ThingException
     */
    @GetMapping("/Thing/Management/sync")
    public ResponseEntity<?> syncAgentAndManagementApp() throws ThingException {
        final Management thingManagement = readManagementSettings();
        if (thingManagement != null) {
            switch (thingManagement.getManagementMode()) {
                case Pull:
                    // consumer application uses REST API to control/read agent data.
                    return new ResponseEntity<Boolean>(true, HttpStatus.OK);
                case Push:
                    // agent pushes data towards consumer. This depends on server application type.
                    final Boolean syncStatus = ManagementAppFactory.getManagementApp(thingManagement).sync();
                    if (syncStatus != null) {
                        return new ResponseEntity<Boolean>(syncStatus, HttpStatus.OK);
                    }
                    return new ResponseEntity<String>("Failed to activate PUSH management type",
                            HttpStatus.BAD_REQUEST);
                default:
                    // default is Unknown. This does nothing.
                    return new ResponseEntity<String>("Unknown management type cannot be activated",
                            HttpStatus.METHOD_NOT_ALLOWED);
            }
        } else {
            return new ResponseEntity<String>("No thing mangement settings found", HttpStatus.FAILED_DEPENDENCY);
        }
    }

    private Management readManagementSettings() {
        Management thingManagement = new Management();
        try {
            thingManagement = ConfigurationReader.getConfiguration().getManagement();
        } catch (ThingException e) {
            // indicate null as problem with reading configuration from JSON thing file.
            thingManagement = null;
        }

        return thingManagement;
    }

    private boolean updateManagementConfiguration(Management management) {
        // update file only if it exists
        if (ThingUtils.fileExists(
                ThingProperties.getInstance().getConfigsRootPath() + ConfigurationReader.CONFIGURATION_FILE_NAME)) {
            // read thing configuration
            try {
                final Thing thingConfiguration = ConfigurationReader.getConfiguration();
                // update management section
                thingConfiguration.setManagement(management);
                // now write updated configuration to file
                final ObjectMapper mapper = new ObjectMapper();
                // make it nicely formatted
                mapper.writerWithDefaultPrettyPrinter();
                mapper.writeValue(new File(ThingProperties.getInstance().getConfigsRootPath()
                        + ConfigurationReader.CONFIGURATION_FILE_NAME), thingConfiguration);
                // at this point indicate success
                return true;
            } catch (ThingException e) {
                IoTLogger.getInstance().error(
                        "Failed to read agent/thing configuration file " + ConfigurationReader.CONFIGURATION_FILE_NAME);
            } catch (Exception e) {
                IoTLogger.getInstance().error(
                        "Failed to write to agent configuration file " + ConfigurationReader.CONFIGURATION_FILE_NAME);
            }
        }
        return false;
    }
}
