package homeprime.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.properties.ThingProperties;
import homeprime.core.system.ThingInfo;

/**
 * Spring REST controller for thing info definition.
 * 
 * @author Milan Ramljak
 */
@RestController
public class ThingController {

	@RequestMapping("/")
	public ThingInfo index() {
		return new ThingInfo();
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
