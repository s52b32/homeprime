package homeprime.items.contact.config.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Types of contact.
 *
 * @author Milan Ramljak
 *
 */
public enum ContactType {

    /**
     * Contact device is wall switch.
     */
    WallSwitch,
    /**
     * Contact device is motion sensor.
     */
    MotionSwitch,
    /**
     * Contact device is magnetic sensor.
     */
    MagneticSwitch,
    /**
     * Contact device is push switch.
     */
    PushSwitch,
    /**
     * Contact device is any (generic) type of contact.
     */
    Generic,
    /**
     * A placeholder for invalid contact type value.
     */
    Unknown;

    @JsonCreator
    public static ContactType fromString(String contactType) {
        try {
            return valueOf(contactType);
        } catch (Exception e) {
        }
        return ContactType.Unknown;
    }

}
