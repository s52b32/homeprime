package homeprime.core.exceptions;

import homeprime.core.logger.IoTLogger;

public class ThingException extends Exception {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 2835653969725288138L;

	public ThingException(String message) {
		super(message);
		IoTLogger.getInstance().info("EXCEPTION: " + message);
	}

	public ThingException(String message, Throwable cause) {
		super(message, cause);
		IoTLogger.getInstance().info("EXCEPTION: " + message);
	}

}
