package homeprime.system.beagleboneblack;

import homeprime.core.exceptions.ThingException;
import homeprime.system.config.ThingMemoryInfo;

/**
 * Default implementation for thing memory info data retrieval on
 * BeagleBoneBlack.
 * 
 * @author Milan Ramljak
 * 
 */
public class ThingMemoryInfoImpl implements ThingMemoryInfo {

	@Override
	public long getTotalMemory() throws ThingException {
		throw new ThingException("ERROR ThingMemoryInfoImpl.getTotalMemory() Not implemented for BeagleBoneBlack");
	}

	@Override
	public long getUsedMemory() throws ThingException {
		throw new ThingException("ERROR ThingMemoryInfoImpl.getUSedMemory() Not implemented for BeagleBoneBlack");
	}

	@Override
	public long getFreeMemory() throws ThingException {
		throw new ThingException("ERROR ThingMemoryInfoImpl.getFreeMemory() Not implemented for BeagleBoneBlack");
	}

	@Override
	public long getSharedMemory() throws ThingException {
		throw new ThingException("ERROR ThingMemoryInfoImpl.getSharedMemory() Not implemented for BeagleBoneBlack");
	}

	@Override
	public Boolean clearCache() throws ThingException {
		throw new ThingException("ERROR ThingMemoryInfoImpl.clearCache() Not implemented for BeagleBoneBlack");
	}

}
