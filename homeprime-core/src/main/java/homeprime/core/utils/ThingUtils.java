package homeprime.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import homeprime.agent.config.enums.OsType;
import homeprime.agent.config.enums.SystemType;
import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.readers.config.ConfigurationReader;
import homeprime.core.properties.ThingProperties;

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
		return new String(Files.readAllBytes(Paths.get(pathname)));
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
	 * Create all directories in provided path.
	 * 
	 * @param dirPath path to create
	 * @return {@code true} if created, otherwise {@code false}
	 */
	public static boolean checkCreateDirs(final String dirPath) {
		if (dirPath != null) {
			final File f = new File(dirPath);
			try {
				return f.mkdirs();
			} catch (Exception e) {
				IoTLogger.getInstance().error("Failed to create direcroty structure: " + e.getMessage());
			}
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
	 * System type is pre-defined in configuration file. Might be better to detect
	 * it while starting the app.
	 */
	public static SystemType detectThingSystemType() {
		SystemType thingSystemType = null;
		try {
			thingSystemType = ConfigurationReader.getConfiguration().getAgent().getType();
			if (thingSystemType == null) {
				IoTLogger.getInstance()
						.info("ERROR ThingProperties.detectThingSystemType() Failed to get thing system type");
				thingSystemType = SystemType.Unknown;
			}
		} catch (ThingException e) {
			IoTLogger.getInstance().error("ThingProperties.detectThingSystemType() Failed to read "
					+ ThingProperties.getInstance().getConfigsRootPath() + ConfigurationReader.CONFIGURATION_FILE_NAME);
			thingSystemType = SystemType.Unknown;
		}
		return thingSystemType;
	}

	public static Set<String> listDirectories(String dir) {
		return Stream.of(new File(dir).listFiles()).filter(file -> file.isDirectory()).map(File::getName)
				.collect(Collectors.toSet());
	}

	public static Set<String> listFiles(String dir) {
		return Stream.of(new File(dir).listFiles()).filter(file -> !file.isDirectory()).map(File::getName)
				.collect(Collectors.toSet());
	}

}
