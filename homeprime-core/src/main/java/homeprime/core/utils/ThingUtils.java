package homeprime.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import homeprime.core.logger.IoTLogger;
import homeprime.core.properties.ThingProperties;
import homeprime.core.system.config.enums.OsType;
import homeprime.core.system.config.enums.ThingSystemType;

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

	/**
	 * Helper method which creates random UUID and writes it to
	 * {@code configs/thing.uuid} file.
	 * 
	 * @return uuid as string
	 */
	public static String generateThingUuid() {
		final Path file = Paths.get(ThingProperties.getInstance().getThingConfigPath() + "thing.uuid");
		final UUID generatedId = UUID.randomUUID();
		try {
			Files.write(file, generatedId.toString().getBytes());
		} catch (IOException e) {
			IoTLogger.getInstance().error("ThingUtils.generateThingUuid() Failed to write UUID to: "
					+ ThingProperties.getInstance().getThingConfigPath() + "thing.uuid");
			System.exit(1);
		}
		return generatedId.toString();
	}

	/**
	 * Helper method for thing system type detection. Uses local session to execute
	 * {@code uname -n} command
	 */
	public static ThingSystemType detectThingSystemType() {
		ThingSystemType thingSystemType = ThingSystemType.Unknown;
		String nodename = null;
		try {
			nodename = ThingUtils.readFile(ThingProperties.getInstance().getThingConfigPath() + "thing.info");
			if (nodename == null) {
				IoTLogger.getInstance()
						.info("ERROR ThingProperties.detectThingSystemType() Failed to get thing system type from "
								+ ThingProperties.getInstance().getThingConfigPath() + "thing.info");
				thingSystemType = ThingSystemType.Unknown;
			} else if (nodename.contains("raspberrypi")) {
				thingSystemType = ThingSystemType.RaspberryPi;
			} else if (nodename.contains("bannanapi")) {
				thingSystemType = ThingSystemType.BananaPi;
			} else if (nodename.contains("beagleboneblack")) {
				thingSystemType = ThingSystemType.BeagleBoneBlack;
			} else if (nodename.contains("mock")) {
				thingSystemType = ThingSystemType.Mock;
			} else {
				IoTLogger.getInstance().info(
						"ERROR ThingProperties.detectThingSystemType() Detect system type returned unknown value: "
								+ nodename);
				thingSystemType = ThingSystemType.Unknown;
			}
		} catch (IOException e) {
			IoTLogger.getInstance().error("ThingProperties.detectThingSystemType() Failed to read "
					+ ThingProperties.getInstance().getThingConfigPath() + "thing.info");
			thingSystemType = ThingSystemType.Unknown;
		}
		return thingSystemType;
	}
}
