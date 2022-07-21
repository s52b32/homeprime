package homeprime.core.exception;

public class ThingException extends Exception {

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = 2835653969725288138L;

    public ThingException(String message) {
        super(message);
    }

    public ThingException(String message, Throwable cause) {
        super(message, cause);
        cause.printStackTrace();
    }

}
