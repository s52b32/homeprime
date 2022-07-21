package homeprime.management;

/**
 * 
 * @author Milan Ramljak
 *
 */
public interface IManagement {

	Boolean getContactState(String itemName);

	Boolean setContactState(String itemName, boolean state);

	Boolean getRelayState();

	Boolean setRelayState();

}
