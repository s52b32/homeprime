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

	public synchronized final String execute(final String command) {
		return executeCommand(command);
	}

	private static String executeCommand(String command) {
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
