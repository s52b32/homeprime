package homeprime.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homeprime.agent.config.pojos.Thing;
import homeprime.core.commander.CmdResponse;
import homeprime.core.commander.LocalCmdExecutionFactory;
import homeprime.core.exception.ThingException;
import homeprime.core.model.readers.config.ConfigurationReader;
import homeprime.core.properties.ThingProperties;

/**
 * Spring REST controller for thing info definition.
 *
 * @author Milan Ramljak
 */
@RestController
public class ThingController {

    public static String agentRevision = "R7A06";

    @RequestMapping("/")
    public String index() {
        return "HomePrime Agent " + agentRevision;
    }

    @RequestMapping("/Thing")
    public Thing thing() {
        Thing thingConfiguration = new Thing();
        try {
            thingConfiguration = ConfigurationReader.getConfiguration();
            // set version based on project version
            thingConfiguration.setVersion(agentRevision);
        } catch (ThingException e) {
        }
        return thingConfiguration;
    }

    /**
     * Terminate Agent REST service.
     *
     * @return 200 OK
     */
    @RequestMapping("/Thing/terminate")
    public ResponseEntity<String> terminate() {
        ThingProperties.getInstance().setMaintenanceState(true);
        System.exit(0); // terminate application with success exit code
        return new ResponseEntity<String>("Terminate", HttpStatus.OK);
    }

    /**
     * Restart homeprime service.
     *
     * @return 200 OK
     * @throws ThingException
     */
    @RequestMapping("/Thing/restart")
    public ResponseEntity<String> restart() throws ThingException {
        // change to maintenance
        ThingProperties.getInstance().setMaintenanceState(true);
        // perform service restart in background. Otherwise termination will kill process starting it.
        final String serviceRestartCmd = "nohup sudo service " + ThingProperties.SERVICE_NAME + " restart";
        // perform command
        final CmdResponse cmdResponse = LocalCmdExecutionFactory.getLocalSession().execute(serviceRestartCmd);
        if (cmdResponse.getExitCode() == 0) {
            return new ResponseEntity<String>("Service Restart - initiated", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Failed to initiate service restart", HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * Reboot the thing.
     *
     * @return 200 OK
     * @throws ThingException
     */
    @RequestMapping("/Thing/reboot")
    public ResponseEntity<String> reboot() throws ThingException {
        // change to maintenance
        ThingProperties.getInstance().setMaintenanceState(true);
        // perform reboot after 5 seconds
        final String serviceRestartCmd = "( sleep 5 ; sudo reboot ) &";
        // perform command
        final CmdResponse cmdResponse = LocalCmdExecutionFactory.getLocalSession().execute(serviceRestartCmd);
        if (cmdResponse.getExitCode() == 0) {
            return new ResponseEntity<String>("System Reboot - initiated", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Failed to initiate system reboot", HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * Enable maintenance mode on the thing.
     *
     * @return 200 OK
     */
    @RequestMapping("/Thing/Maintenance/on")
    public ResponseEntity<String> enableThingMaintenace() {
        ThingProperties.getInstance().setMaintenanceState(true);
        return new ResponseEntity<String>("Maintenance Mode - enabled", HttpStatus.OK);
    }

    /**
     * Disable maintenance mode on the thing.
     *
     * @return 200 OK
     */
    @RequestMapping("/Thing/Maintenance/off")
    public ResponseEntity<String> disableThingMaintenace() {
        ThingProperties.getInstance().setMaintenanceState(false);
        return new ResponseEntity<String>("Maintenance Mode - disabled", HttpStatus.OK);
    }

    /**
     * Get maintenance mode of the thing.
     *
     * @return 200 OK with true / false depending if maintenance is enabled /
     *         disabled
     */
    @RequestMapping("/Thing/Maintenance/read")
    public ResponseEntity<String> getThingMaintenaceState() {
        return new ResponseEntity<String>(ThingProperties.getInstance().getMaintenanceState().toString(),
                HttpStatus.OK);
    }

}
