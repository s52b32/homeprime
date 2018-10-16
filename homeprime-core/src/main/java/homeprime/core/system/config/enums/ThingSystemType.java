package homeprime.core.system.config.enums;

/**
 * Types of system running homeprime agent service.
 * 
 * @author Milan Ramljak
 * 
 */
public enum ThingSystemType {
	/**
	 * Raspberry PI models 2 & 3 are tested.
	 */
	RaspberryPi,
	/**
	 * Banana PI.
	 */
	BananaPi,
	/**
	 * Beagle Bone Black
	 */
	BeagleBoneBlack,
	/**
	 * Mocked thing type used for testing client.
	 */
	Mock,
	/**
	 * In case of used is not known.
	 */
	Unknown

}
