package homeprime.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.List;
import java.util.Scanner;

import homeprime.core.logger.IoTLogger;
import homeprime.core.system.config.enums.OsType;

/**
 * Utilities class holding common helper methods
 * 
 * @author Milan Ramljak
 * 
 */
public class ThingUtils {

	/**
	 * Read content of the provided file.
	 * 
	 * @param pathname full path to file
	 * @return string holding file content
	 * @throws IOException in case of failure in file read procedure
	 */
	public static String readFile(String pathname) throws IOException {

		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");

		try {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}

	/**
	 * Check if file exists and is not directory.
	 * 
	 * @param filePath full path to file
	 * @return {@code true} if file exists, otherwise {@code false}
	 */
	public static Boolean fileExists(String filePath) {
		final File f = new File(filePath);
		if (f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}

	/**
	 * Extract IP address of eth0 interface on running machine.
	 * 
	 * @return machine IP address on interface eth0
	 */
	public static String getLocalIpAddress() {
		NetworkInterface networkInterface = null;
		String ipAddress = "";
		try {
			networkInterface = NetworkInterface.getByName("eth0");
			List<InterfaceAddress> inetAddresses = networkInterface.getInterfaceAddresses();
			for (InterfaceAddress interfaceAddress : inetAddresses) {
				IoTLogger.getInstance().info("Local IP Address: " + interfaceAddress.getAddress());
				ipAddress = interfaceAddress.getAddress().toString().replaceAll("/", "");
			}
		} catch (SocketException e) {
			IoTLogger.getInstance().info("SocketException on getting local IP address.");
		}
		return ipAddress;
	}

	/**
	 * Get type of OS from JVM properties.
	 * 
	 * @return {@link OsType}
	 */
	public static OsType getOsType() {
		final OsType osType = OsType.Unknown;
		final String osName = System.getProperty("os.name");
		if (osName != null) {
			if (osName.contains("Linux")) {
				return OsType.Unix;
			} else if (osName.contains("Windows")) {
				return OsType.Windows;
			}
		}
		return osType;

	}
}
