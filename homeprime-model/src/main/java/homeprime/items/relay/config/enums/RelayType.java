package homeprime.items.relay.config.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Type of relay channel.
 * 
 * @author Milan Ramljak
 * 
 */
public enum RelayType {

	/**
	 * Normally closed.
	 */
	NC,
	/**
	 * Normally opened.
	 */
	NO,
	/**
	 * Information not provided.
	 */
	Unknown;

	@JsonCreator
	public static RelayType fromString(String param) {
		try {
			return valueOf(param);
		} catch (Exception e) {
		}
		return RelayType.Unknown;
	}

}
