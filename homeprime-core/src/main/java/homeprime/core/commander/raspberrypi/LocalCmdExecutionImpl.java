package homeprime.core.commander.raspberrypi;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import homeprime.core.commander.CmdResponse;
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

	public synchronized final CmdResponse execute(final String command) {
		return executeCommand(command);
	}

	private static CmdResponse executeCommand(String command) {
		CmdResponse cmdResponse = null;
		String response = null;
		try {
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(command);
			pr.waitFor();

			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

			String line = null;

			while ((line = input.readLine()) != null) {
				response = response + "\n" + line;
			}

			cmdResponse = new CmdResponse(response, pr.waitFor());

		} catch (Exception e) {
			IoTLogger.getInstance().error("Exception happen while executing command: " + command);
			e.printStackTrace();
		}
		return cmdResponse;
	}

}
