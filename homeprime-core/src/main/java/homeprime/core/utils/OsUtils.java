package homeprime.core.utils;

import java.net.InetAddress;

public class OsUtils {
	private static String os = null;

	/**
	 * Get running OS name.
	 * 
	 * @return OS name or {@code null} if not detected
	 */
	public static String getOsName() {
		if (os == null) {
			os = System.getProperty("os.name");
		}
		return os;
	}

	/**
	 * Detect if running host is Windows or not.
	 * 
	 * @return {@code true} if Windows otherwise {@code false}
	 */
	public static boolean isWindows() {
		return getOsName().startsWith("Windows");
	}

	/**
	 * Get host name.
	 * 
	 * @return name of the host or {@code null} if not found
	 */
	public static String getHostName() {
		try {
			// get system name
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception E) {
			return null;
		}
	}

}