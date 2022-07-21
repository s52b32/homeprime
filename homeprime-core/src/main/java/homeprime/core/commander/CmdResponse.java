package homeprime.core.commander;

/**
 *
 * @author Milan Ramljak
 *
 */
public class CmdResponse {

    private String response = null;
    private int exitCode = -1;
    private int pid = -1;

    public CmdResponse(String response, int exitCode) {
        this.response = response;
        this.exitCode = exitCode;
    }

    public String getResponse() {
        return response;
    }

    public int getExitCode() {
        return exitCode;
    }

    public int getPid() {
        return pid;
    }

}
