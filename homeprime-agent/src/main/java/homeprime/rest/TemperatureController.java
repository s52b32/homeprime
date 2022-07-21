package homeprime.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.exception.ThingException;
import homeprime.core.model.readers.items.temperature.TemperatureConfigReader;
import homeprime.items.temperature.TemperatureSensorControllerFactory;
import homeprime.items.temperature.config.pojos.TemperatureSensor;
import homeprime.items.temperature.config.pojos.TemperatureSensors;

/**
 * Spring REST controller for thing temperature sensors.
 * 
 * @author Milan Ramljak
 * 
 */
@RestController
public class TemperatureController {

	@RequestMapping("/Thing/Temperatures")
	public ResponseEntity<?> getTemperatureSensors() {
		TemperatureSensors temperatureSensors = null;
		try {
			temperatureSensors = TemperatureConfigReader.getTemperatureSensors();
		} catch (ThingException e1) {
			return new ResponseEntity<String>("No temperature sensors defined or configuration is not valid!",
					HttpStatus.NO_CONTENT);
		}

		if (temperatureSensors != null) {
			try {
				final List<TemperatureSensor> temperatureSensorsList = temperatureSensors.getTemperatureSensors();
				for (TemperatureSensor temperatureSensor : temperatureSensorsList) {
					temperatureSensor.setValue(TemperatureSensorControllerFactory.getTemperatureSensorReader()
							.readTemperature(temperatureSensor));
				}
				// update with new data
				temperatureSensors.setTemperatureSensors(temperatureSensorsList);
				// success
				return new ResponseEntity<TemperatureSensors>(temperatureSensors, HttpStatus.OK);
			} catch (ThingException e) {
				return new ResponseEntity<String>("Failed to read temperature sensor value!",
						HttpStatus.EXPECTATION_FAILED);
			}
		} else {
			return new ResponseEntity<String>("No available temperature sensors!", HttpStatus.NO_CONTENT);
		}

	}

	@RequestMapping("/Thing/Temperatures/reload-config")
	public ResponseEntity<String> syncTemperatureConfig() {
		TemperatureConfigReader.reloadConfig();
		return new ResponseEntity<String>("Temperature config reloaded", HttpStatus.OK);
	}

	@RequestMapping("/Thing/Temperatures/{temperatureSensorId}")
	public ResponseEntity<?> getTemperatureSensorById(
			@PathVariable(value = "temperatureSensorId") int temperatureSensorId) {
		try {
			final TemperatureSensor findTemperatureSensorById = findTemperatureSensorById(temperatureSensorId);
			findTemperatureSensorById.setValue(TemperatureSensorControllerFactory.getTemperatureSensorReader()
					.readTemperature(findTemperatureSensorById));
			// success
			return new ResponseEntity<TemperatureSensor>(findTemperatureSensorById, HttpStatus.OK);
		} catch (ThingException e) {
			return new ResponseEntity<String>("Failed to get temperature sensor value!", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/Temperatures/{temperatureSensorId}/read")
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
