package homeprime.core.logger;

import org.apache.log4j.Logger;

/**
 * Main logger.
 * 
 * @author Milan Ramljak
 * 
 */
public class IoTLogger {

	private Logger logger = Logger.getLogger(IoTLogger.class);
	private static IoTLogger instance = null;

	/**
	 * 
	 * Hidden constructor.
	 */
	private IoTLogger() {
	}

	public static IoTLogger getInstance() {
		if (instance == null) {
			instance = new IoTLogger();
		}
		return instance;
	}

	public void info(final String message) {
		logger.info(message);
	}

	public void error(final String message) {
		logger.error(message);
	}

	public void warn(final String message) {
		logger.warn(message);
	}
}
