package homeprime.system.raspberrypi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import homeprime.core.commander.CmdResponse;
import homeprime.core.commander.LocalCmdExecution;
import homeprime.core.commander.LocalCmdExecutionFactory;
import homeprime.core.exception.ThingException;
import homeprime.system.ThingDiskInfo;
import homeprime.system.ThingHardwareInfo;
import homeprime.system.ThingMemoryInfo;
import homeprime.system.ThingOsInfo;
import homeprime.system.ThingSystemInfo;

/**
 * Default implementation for thing system info data retrieval on Raspberry PI.
 *
 * @author Milan Ramljak
 *
 */
public class ThingSystemInfoImpl implements ThingSystemInfo {

    @Override
    public ThingHardwareInfo getThingHardwareInfo() throws ThingException {
        return new ThingHardwareInfoImpl();
    }

    @Override
    public ThingMemoryInfo getThingMemoryInfo() throws ThingException {
        return new ThingMemoryInfoImpl();
    }

    @Override
    public ThingOsInfo getThingOsInfo() throws ThingException {
        return new ThingOSInfoImpl();
    }

    @Override
    public ThingDiskInfo getThingDiskInfo() throws ThingException {
        return new ThingDiskInfoImpl();
    }

    @Override
    public Long getSystemUptime() throws ThingException {
        LocalCmdExecution localCmdExecution = LocalCmdExecutionFactory.getLocalSession();
        final CmdResponse uptimeSinceResponse = localCmdExecution.execute("uptime -s");
        if (uptimeSinceResponse != null && uptimeSinceResponse.getExitCode() == 0) {
            // example response: 2019-04-18 23:26:49
            SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                Date lFromDate1 = datetimeFormatter1.parse(uptimeSinceResponse.getResponse());
                return System.currentTimeMillis() - lFromDate1.toInstant().toEpochMilli();
            } catch (ParseException e) {
                throw new ThingException("Failed to parse system uptime response", e);
            }
        }
        return null;
    }

}
