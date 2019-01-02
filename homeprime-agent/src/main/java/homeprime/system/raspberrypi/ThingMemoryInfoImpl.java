package homeprime.system.raspberrypi;

import java.io.IOException;

import com.pi4j.system.SystemInfo;

import homeprime.core.commander.CmdResponse;
import homeprime.core.commander.LocalCmdExecution;
import homeprime.core.commander.LocalCmdExecutionFactory;
import homeprime.core.exceptions.ThingException;
import homeprime.system.config.ThingMemoryInfo;

/**
 * Default implementation for thing memory info data retrieval on Raspberry PI.
 * 
 * @author Milan Ramljak
 */
public class ThingMemoryInfoImpl implements ThingMemoryInfo {

	@Override
	public long getTotalMemory() throws ThingException {
		try {
			return SystemInfo.getMemoryTotal();
		} catch (UnsupportedOperationException e) {
			throw new ThingException("Failed to get RaspberryPi total memory", e);
		} catch (IOException e) {
			throw new ThingException("Failed to get RaspberryPi total memory", e);
		} catch (InterruptedException e) {
			throw new ThingException("Failed to get RaspberryPi total memory", e);
		}
	}

	@Override
	public long getUsedMemory() throws ThingException {
		try {
			return SystemInfo.getMemoryUsed();
		} catch (UnsupportedOperationException e) {
			throw new ThingException("Failed to get RaspberryPi used memory", e);
		} catch (IOException e) {
			throw new ThingException("Failed to get RaspberryPi used memory", e);
		} catch (InterruptedException e) {
			throw new ThingException("Failed to get RaspberryPi used memory", e);
		}
	}

	@Override
	public long getFreeMemory() throws ThingException {
		try {
			return SystemInfo.getMemoryFree();
		} catch (UnsupportedOperationException e) {
			throw new ThingException("Failed to get RaspberryPi free memory", e);
		} catch (IOException e) {
			throw new ThingException("Failed to get RaspberryPi free memory", e);
		} catch (InterruptedException e) {
			throw new ThingException("Failed to get RaspberryPi free memory", e);
		}
	}

	@Override
	public long getSharedMemory() throws ThingException {
		try {
			return SystemInfo.getMemoryShared();
		} catch (UnsupportedOperationException e) {
			throw new ThingException("Failed to get RaspberryPi shared memory", e);
		} catch (IOException e) {
			throw new ThingException("Failed to get RaspberryPi shared memory", e);
		} catch (InterruptedException e) {
			throw new ThingException("Failed to get RaspberryPi shared memory", e);
		}
	}

	@Override
	public Boolean clearCache() throws ThingException {
		boolean clearedFlag = false;
		final String clearCachesCommand = "sudo echo 1 >/proc/sys/vm/drop_caches";
		LocalCmdExecution localCmdExecution = LocalCmdExecutionFactory.getLocalSession();
		final CmdResponse cmdResponse = localCmdExecution.execute(clearCachesCommand);
		if (cmdResponse != null && cmdResponse.getExitCode() == 0) {
			clearedFlag = true;
		}
		// send to GC
		localCmdExecution = null;
		return clearedFlag;

	}

}
