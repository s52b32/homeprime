package homeprime.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.commander.LocalCmdExecution;
import homeprime.core.commander.LocalCmdExecutionFactory;
import homeprime.core.exceptions.ThingException;
import homeprime.system.ThingSystemInfoFactory;
import homeprime.system.config.ThingHardwareInfo;
import homeprime.system.config.ThingMemoryInfo;
import homeprime.system.config.ThingOsInfo;
import homeprime.system.config.ThingSystemInfo;

/**
 * Spring REST controller for thing system info.
 * 
 * @author Milan Ramljak
 * 
 */
@RestController
public class SystemInfoController {

	@RequestMapping("/Thing/System")
	public ResponseEntity<ThingSystemInfo> getSystemInfo() {
		try {
			return new ResponseEntity<ThingSystemInfo>(ThingSystemInfoFactory.getThingSystemInfo(), HttpStatus.OK);
		} catch (ThingException e) {
			return new ResponseEntity<ThingSystemInfo>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/System/Os")
	public ResponseEntity<ThingOsInfo> getOSInfo() {
		try {
			return new ResponseEntity<ThingOsInfo>(ThingSystemInfoFactory.getThingSystemInfo().getThingOsInfo(),
					HttpStatus.OK);
		} catch (ThingException e) {
			return new ResponseEntity<ThingOsInfo>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/System/OsName")
	public ResponseEntity<String> getOSName() {
		try {
			return new ResponseEntity<String>(ThingSystemInfoFactory.getThingSystemInfo().getThingOsInfo().getOsName(),
					HttpStatus.OK);
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get OS name. Exception occured: " + e.getMessage());
		}
	}

	@RequestMapping("/Thing/System/OsVersion")
	public ResponseEntity<String> getOSVersion() {
		try {
			return new ResponseEntity<String>(
					ThingSystemInfoFactory.getThingSystemInfo().getThingOsInfo().getOsVersion(), HttpStatus.OK);
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get OS version. Exception occured: " + e.getMessage());
		}
	}

	@RequestMapping("/Thing/System/OsArchitecture")
	public ResponseEntity<String> getOSArchitecture() {
		try {
			return new ResponseEntity<String>(
					String.valueOf(ThingSystemInfoFactory.getThingSystemInfo().getThingOsInfo().getOsArchitecture()),
					HttpStatus.OK);
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get hardware CPU temperature. Exception occured: " + e.getMessage());
		}
	}

	@RequestMapping("/Thing/System/OsFirmwareBuild")
	public ResponseEntity<String> getOSFirmwareBuild() {
		try {
			return new ResponseEntity<String>(
					String.valueOf(ThingSystemInfoFactory.getThingSystemInfo().getThingOsInfo().getOsFirmwareBuild()),
					HttpStatus.OK);
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get OS firmware build. Exception occured: " + e.getMessage());
		}
	}

	@RequestMapping("/Thing/System/OsFirmwareDate")
	public ResponseEntity<String> getOSFirmwareDate() {
		try {
			return new ResponseEntity<String>(
					String.valueOf(ThingSystemInfoFactory.getThingSystemInfo().getThingOsInfo().getOsFirmwareDate()),
					HttpStatus.OK);
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get OS firmware date. Exception occured: " + e.getMessage());
		}
	}

	@RequestMapping("/Thing/System/Memory")
	public ResponseEntity<ThingMemoryInfo> getMemoryInfo() {
		try {
			return new ResponseEntity<ThingMemoryInfo>(ThingSystemInfoFactory.getThingSystemInfo().getThingMemoryInfo(),
					HttpStatus.OK);
		} catch (ThingException e) {
			return new ResponseEntity<ThingMemoryInfo>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/System/clear-cache")
	public ResponseEntity<Boolean> clearMemory() {
		try {
			return new ResponseEntity<Boolean>(
					ThingSystemInfoFactory.getThingSystemInfo().getThingMemoryInfo().clearCache(), HttpStatus.OK);
		} catch (ThingException e) {
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/System/TotalMemory")
	public ResponseEntity<String> getTotalMemory() {
		try {
			return new ResponseEntity<String>(
					String.valueOf(ThingSystemInfoFactory.getThingSystemInfo().getThingMemoryInfo().getTotalMemory()),
					HttpStatus.OK);
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get total system memory. Exception occured: " + e.getMessage());
		}
	}

	@RequestMapping("/Thing/System/FreeMemory")
	public ResponseEntity<String> getFreeMemory() {
		try {
			return new ResponseEntity<String>(
					String.valueOf(ThingSystemInfoFactory.getThingSystemInfo().getThingMemoryInfo().getFreeMemory()),
					HttpStatus.OK);
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get system free memory. Exception occured: " + e.getMessage());
		}
	}

	@RequestMapping("/Thing/System/UsedMemory")
	public ResponseEntity<String> getUsedMemory() {
		try {
			return new ResponseEntity<String>(
					String.valueOf(ThingSystemInfoFactory.getThingSystemInfo().getThingMemoryInfo().getUsedMemory()),
					HttpStatus.OK);
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get system used memory. Exception occured: " + e.getMessage());
		}
	}

	@RequestMapping("/Thing/System/SharedMemory")
	public ResponseEntity<String> getSharedMemory() {
		try {
			return new ResponseEntity<String>(
					String.valueOf(ThingSystemInfoFactory.getThingSystemInfo().getThingMemoryInfo().getSharedMemory()),
					HttpStatus.OK);
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get system shared memory. Exception occured: " + e.getMessage());
		}
	}

	@RequestMapping("/Thing/System/FreeStorageMemory")
	public ResponseEntity<String> getFreeStorageMemory() {
		try {
			LocalCmdExecution localCmdExecution = LocalCmdExecutionFactory.getLocalSession();
			final String storageStatus = localCmdExecution.getStorageStatus();
			if (storageStatus != null && storageStatus.contains(":")) {
				final String[] memoryData = storageStatus.split(":");
				final Integer available = Integer.parseInt(memoryData[1]);
				return new ResponseEntity<String>(available.toString(), HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Failed to get system storage available memory");
			}
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get system storage available memory. Exception occured: " + e.getMessage());
		}
	}

	@RequestMapping("/Thing/System/TotalStorageMemory")
	public ResponseEntity<String> getTotalStorageMemory() {
		try {
			LocalCmdExecution localCmdExecution = LocalCmdExecutionFactory.getLocalSession();
			final String storageStatus = localCmdExecution.getStorageStatus();
			if (storageStatus != null && storageStatus.contains(":")) {
				final String[] memoryData = storageStatus.split(":");
				final Integer available = Integer.parseInt(memoryData[0]);
				return new ResponseEntity<String>(available.toString(), HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to get system storage total memory");
			}
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get system storage total memory. Exception occured: " + e.getMessage());
		}
	}

	@RequestMapping("/Thing/System/Hardware")
	public ResponseEntity<ThingHardwareInfo> getHardwareInfo() {
		try {
			return new ResponseEntity<ThingHardwareInfo>(
					ThingSystemInfoFactory.getThingSystemInfo().getThingHardwareInfo(), HttpStatus.OK);
		} catch (ThingException e) {
			return new ResponseEntity<ThingHardwareInfo>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/Thing/System/SerialNumber")
	public ResponseEntity<String> getSerialNumber() {
		try {
			return new ResponseEntity<String>(
					ThingSystemInfoFactory.getThingSystemInfo().getThingHardwareInfo().getSerialNumber(),
					HttpStatus.OK);
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get hardware serial number. Exception occured: " + e.getMessage());
		}
	}

	@RequestMapping("/Thing/System/BoardType")
	public ResponseEntity<String> getBoardType() {
		try {
			return new ResponseEntity<String>(
					ThingSystemInfoFactory.getThingSystemInfo().getThingHardwareInfo().getBoardType(), HttpStatus.OK);
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get hardware board type. Exception occured: " + e.getMessage());
		}
	}

	@RequestMapping("/Thing/System/CpuTemperature")
	public ResponseEntity<String> getCPUTemperature() {
		try {
			return new ResponseEntity<String>(
					String.valueOf(
							ThingSystemInfoFactory.getThingSystemInfo().getThingHardwareInfo().getCpuTemperature()),
					HttpStatus.OK);
		} catch (ThingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to get hardware CPU temperature. Exception occured: " + e.getMessage());
		}
	}

}
