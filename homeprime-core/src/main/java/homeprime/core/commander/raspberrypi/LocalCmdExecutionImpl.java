package homeprime.core.commander.raspberrypi;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import homeprime.core.commander.LocalCmdExecution;
import homeprime.core.logger.IoTLogger;

/**
 * Unix local CLI session implementation.
 * 
 * @author Milan Ramljak
 */
public class LocalCmdExecutionImpl implements LocalCmdExecution {

	public LocalCmdExecutionImpl() {
	}

	/**
	 * Perform {@code uname -n} command on local session to detect hardware name.
	 * 
	 * @return - node name
	 */
	public String getMachineNodeName() {
		String nodeName = null;
		final String output = execute("uname -n");
		IoTLogger.getInstance().info("Detecting machine node name from 'uname -n' command response: " + output);
		if (output != null) {
			nodeName = output.trim();
		}
		return nodeName;
	}

	public synchronized String getStorageStatus() {
		String diskUsage = null;
		final String output = execute("df /dev/root");
		IoTLogger.getInstance().info("Detecting storage status command response: " + output);
		if (output != null) {
			String[] lines = output.split("\n");
			String[] statusLines = lines[1].split(" ");
			String total = null;
			String available = null;
			int counter = 0;
			for (String string : statusLines) {
				if (!string.equals("")) {
					counter++;
					if (counter == 2) {
						total = string.trim();
					}
					if (counter == 4) {
						available = string.trim();
					}
				}
			}
			diskUsage = total + ":" + available;
		}
		return diskUsage;
	}

	public synchronized String execute(String command) {
		return executeCommand(command);
	}

	private String executeCommand(String command) {
		String response = null;
		try {
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(command);

			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

			String line = null;

			while ((line = input.readLine()) != null) {
				response = response + "\n" + line;
			}

			pr.waitFor();

		} catch (Exception e) {
			IoTLogger.getInstance().error("Exception happen while executiing command: " + command);
			e.printStackTrace();
		}
		return response;
	}

}
