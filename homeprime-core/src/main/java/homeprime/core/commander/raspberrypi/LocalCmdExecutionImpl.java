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

    @Override
    public synchronized final CmdResponse execute(final String command) {
        return executeCommand(command);
    }

    private static CmdResponse executeCommand(String command) {
        CmdResponse cmdResponse = null;
        final StringBuilder output = new StringBuilder();
        IoTLogger.getInstance().info("Execute CMD: " + command);
        try {
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(command);

            pr.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = "";
            while ((line = buf.readLine()) != null) {
                output.append(line + "\n");
            }
            IoTLogger.getInstance().info("CMD output: " + output);
            cmdResponse = new CmdResponse(output.toString(), pr.waitFor());
        } catch (Exception e) {
            IoTLogger.getInstance()
                    .error("Exception (" + e.getMessage() + ") happen while executing command: " + command);
        }
        return cmdResponse;
    }

}
