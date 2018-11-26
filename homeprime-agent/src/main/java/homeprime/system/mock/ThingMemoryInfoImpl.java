package homeprime.system.mock;

import java.util.Random;

import homeprime.core.exceptions.ThingException;
import homeprime.system.config.ThingMemoryInfo;

/**
 * Mocked implementation for thing memory info data retrieval.
 * 
 * @author Milan Ramljak
 */
public class ThingMemoryInfoImpl implements ThingMemoryInfo {

	@Override
	public long getTotalMemory() throws ThingException {
		return new Random().nextInt(100000) + 1000;
	}

	@Override
	public long getUsedMemory() throws ThingException {
		return new Random().nextInt(1000) + 100;
	}

	@Override
	public long getFreeMemory() throws ThingException {
		return new Random().nextInt(1000) + 50;
	}

	@Override
	public long getSharedMemory() throws ThingException {
		return new Random().nextInt(1000);
	}

	@Override
	public Boolean clearCache() throws ThingException {
		System.out.println("Clearing memory cache");
		return true;
	}

}
