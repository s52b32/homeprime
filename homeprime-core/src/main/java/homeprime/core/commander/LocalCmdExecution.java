package homeprime.core.commander;

/**
 * Interface holding Local CLI session exposed methods.
 * 
 * @author Milan Ramljak
 */
public interface LocalCmdExecution {

	/**
	 * Perform command.
	 * 
	 * @param command command to be executed
	 * @return command response object
	 */
	CmdResponse execute(String command);

}
