package homeprime.core.commander;

import homeprime.core.commander.raspberrypi.LocalCmdExecutionImpl;
import homeprime.core.exceptions.ThingException;
import homeprime.core.properties.ThingProperties;

/**
 * Thing local CLI session factory.
 * 
 * @author Milan Ramljak
 */
public class LocalCmdExecutionFactory {

	/**
	 * Hidden constructor.
	 */
	private LocalCmdExecutionFactory() {
	}

	public static LocalCmdExecution getLocalSession() throws ThingException {
		switch (ThingProperties.getInstance().getThingSystemType()) {
		case RaspberryPi:
		case BananaPi:
		case BeagleBoneBlack:
			return new LocalCmdExecutionImpl();
		default:
			throw new ThingException("Thing local session CLI implementation for Unknown cannot be created.");
		}
	}

}
