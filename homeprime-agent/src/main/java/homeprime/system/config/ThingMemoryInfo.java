package homeprime.system.config;

import homeprime.core.exceptions.ThingException;

/**
 * Interface describing thing memory information.
 * 
 * @author Milan Ramljak
 * 
 */
public interface ThingMemoryInfo {

	/**
	 * Get total RAM memory of running machine.
	 * 
	 * @return total RAM memory.
	 * @throws ThingException in case of any failure
	 */
	long getTotalMemory() throws ThingException;

	/**
	 * Get used RAM memory of running machine.
	 * 
	 * @return used RAM memory.
	 * @throws ThingException in case of any failure
	 */
	long getUsedMemory() throws ThingException;

	/**
	 * Get free RAM memory of running machine.
	 * 
	 * @return free RAM memory.
	 * @throws ThingException in case of any failure
	 */
	long getFreeMemory() throws ThingException;

	/**
	 * Get shared RAM memory of running machine.
	 * 
	 * @return shared RAM memory.
	 * @throws ThingException in case of any failure
	 */
	long getSharedMemory() throws ThingException;

	/**
	 * Order machine to clear cache pages.
	 * 
	 * @return {@code true} if successful, otherwise {@code false}
	 * @throws ThingException in case of any failure
	 */
	Boolean clearCache() throws ThingException;

}
