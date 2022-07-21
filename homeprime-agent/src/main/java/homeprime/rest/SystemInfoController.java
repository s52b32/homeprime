package homeprime.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.exception.ThingException;
import homeprime.system.ThingSystemInfo;
import homeprime.system.ThingSystemInfoFactory;

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

    @RequestMapping("/Thing/System/TotalMemory")
    public ResponseEntity<?> getSystemTotalMemory() {
        try {
            return new ResponseEntity<Long>(
                    ThingSystemInfoFactory.getThingSystemInfo().getThingMemoryInfo().getTotalMemory(), HttpStatus.OK);
        } catch (ThingException e) {
            return new ResponseEntity<String>("Failed to get total system memory", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/Thing/System/UsedMemory")
    public ResponseEntity<?> getSystemUsedMemory() {
        try {
            return new ResponseEntity<Long>(
                    ThingSystemInfoFactory.getThingSystemInfo().getThingMemoryInfo().getUsedMemory(), HttpStatus.OK);
        } catch (ThingException e) {
            return new ResponseEntity<String>("Failed to get used system memory", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/Thing/System/TotalDiskSpace")
    public ResponseEntity<?> getTotalDiskSpace() {
        try {
            return new ResponseEntity<Long>(
                    ThingSystemInfoFactory.getThingSystemInfo().getThingDiskInfo().getTotalDiskSpace(), HttpStatus.OK);
        } catch (ThingException e) {
            return new ResponseEntity<String>("Failed to get total disk space", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/Thing/System/FreeDiskSpace")
    public ResponseEntity<?> getFreeDiskSpace() {
        try {
            return new ResponseEntity<Long>(
                    ThingSystemInfoFactory.getThingSystemInfo().getThingDiskInfo().getFreeDiskSpace(), HttpStatus.OK);
        } catch (ThingException e) {
            return new ResponseEntity<String>("Failed to get free disk space", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/Thing/System/CpuTemperature")
    public ResponseEntity<?> getSystemCpuTemperature() {
        try {
            return new ResponseEntity<Float>(
                    ThingSystemInfoFactory.getThingSystemInfo().getThingHardwareInfo().getCpuTemperature(),
                    HttpStatus.OK);
        } catch (ThingException e) {
            return new ResponseEntity<String>("Failed to get system CPU temperature", HttpStatus.BAD_REQUEST);
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

}
