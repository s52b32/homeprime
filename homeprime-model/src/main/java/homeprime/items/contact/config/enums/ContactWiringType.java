package homeprime.items.contact.config.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Types of pull up/down resistor connected to contact/switch.
 *
 * @author Milan Ramljak
 *
 */
public enum ContactWiringType {

    /**
     * Pull up internal resistor.
     */
    PullUp,
    /**
     * Pull down internal resistor.
     */
    PullDown,
    /**
     * Pull up/down is hardware based.
     */
    Off;

    @JsonCreator
    public static ContactWiringType fromString(String wiringType) {
        try {
            return valueOf(wiringType);
        } catch (Exception e) {
        }
        return ContactWiringType.Off;
    }

}
