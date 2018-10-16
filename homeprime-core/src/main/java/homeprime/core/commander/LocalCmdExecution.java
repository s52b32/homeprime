package homeprime.core.commander;

/**
 * Interface holding Local CLI session exposed methods.
 * 
 * @author Milan Ramljak
 */
public interface LocalCmdExecution {

	/**
	 * Detects under laying machine node name.
	 * 
	 * @return machine node name.
	 */
	String getMachineNodeName();

	/**
	 * Get storage status in format total:available.
	 * 
	 * @return
	 */
	String getStorageStatus();

	/**
	 * Perform command.
	 * 
	 * @param command command to be executed
	 * @return command response
	 */
	String execute(String command);

}
