package homeprime.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.exceptions.ThingException;
import homeprime.items.temperature.TemperatureSensorControllerFactory;
import homeprime.items.temperature.config.pojos.TemperatureSensor;
import homeprime.items.temperature.config.pojos.TemperatureSensors;
import homeprime.items.temperature.config.reader.TemperatureConfigReader;

/**
 * Spring REST controller for thing temperature sensors setup.
 * 
 * @author Milan Ramljak
 * 
 */
@RestController
public class TemperatureController {

	@RequestMapping("/Thing/Temperatures")
	public ResponseEntity<TemperatureSensors> getTemperatureSensors() {
		try {
			return new ResponseEntity<TemperatureSensors>(TemperatureConfigReader.getTemperatureSensors(),
					HttpStatus.OK);
		} catch (ThingException e) {
			return new ResponseEntity<TemperatureSensors>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Temperature/{temperatureSensorId}")
	public ResponseEntity<TemperatureSensor> getTemperatureSensorById(
			@PathVariable(value = "temperatureSensorId") int temperatureSensorId) {
		try {
			return new ResponseEntity<TemperatureSensor>(findTemperatureSensorById(temperatureSensorId), HttpStatus.OK);
		} catch (ThingException e) {
			return new ResponseEntity<TemperatureSensor>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Temperature/{temperatureSensorId}/read")
	public ResponseEntity<Float> getTemperatureSensorValue(
			@PathVariable(value = "temperatureSensorId") int temperatureSensorId) {
		try {
			final TemperatureSensor temperatureSensor = findTemperatureSensorById(temperatureSensorId);
			if (temperatureSensor != null) {
				return new ResponseEntity<Float>(TemperatureSensorControllerFactory.getTemperatureSensorReader()
						.readTemperature(temperatureSensor), HttpStatus.OK);
			} else {
				return new ResponseEntity<Float>(HttpStatus.NOT_FOUND);
			}
		} catch (ThingException e) {
			return new ResponseEntity<Float>(HttpStatus.BAD_REQUEST);
		}
	}

	private TemperatureSensor findTemperatureSensorById(int temperatureSensorId) throws ThingException {
		TemperatureSensors temperatureSensorsPojo = null;
		try {
			temperatureSensorsPojo = TemperatureConfigReader.getTemperatureSensors();
		} catch (ThingException e) {
			throw new ThingException("ERROR ThingTemperatureController.findTemperatureSensorById( "
					+ temperatureSensorId + ") Failed to get temperature sensors.", e);
		}
		if (temperatureSensorsPojo != null) {
			final List<TemperatureSensor> temperatureSensors = temperatureSensorsPojo.getTemperatureSensors();
			if (!temperatureSensors.isEmpty()) {
				for (TemperatureSensor temperatureSensor : temperatureSensors) {
					if (temperatureSensor.getId() == temperatureSensorId) {
						return temperatureSensor;
					}
				}
			}
		}
		return null;
	}
}
