package homeprime.system.bannanapi;

import homeprime.core.exceptions.ThingException;
import homeprime.system.config.ThingMemoryInfo;

/**
 * Default implementation for thing memory info data retrieval on Bannana PI.
 * 
 * @author Milan Ramljak
 * 
 */
public class ThingMemoryInfoImpl implements ThingMemoryInfo {

    @Override
    public long getTotalMemory() throws ThingException {
	throw new ThingException("ERROR ThingMemoryInfoImpl.getTotalMemory() Not implemented for BannanaPi");
    }

    @Override
    public long getUsedMemory() throws ThingException {
	throw new ThingException("ERROR ThingMemoryInfoImpl.getUSedMemory() Not implemented for BannanaPi");
    }

    @Override
    public long getFreeMemory() throws ThingException {
	throw new ThingException("ERROR ThingMemoryInfoImpl.getFreeMemory() Not implemented for BannanaPi");
    }

    @Override
    public long getSharedMemory() throws ThingException {
	throw new ThingException("ERROR ThingMemoryInfoImpl.getSharedMemory() Not implemented for BannanaPi");
    }

    @Override
    public Boolean clearCache() throws ThingException {
	throw new ThingException("ERROR ThingMemoryInfoImpl.clearCache() Not implemented for BannanaPi");
    }

}
