package homeprime.items.contact.config.pojos;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import homeprime.items.contact.config.enums.ContactConnectionType;
import homeprime.items.contact.config.enums.ContactTriggerType;
import homeprime.items.contact.config.enums.ContactType;
import homeprime.items.contact.config.enums.ContactWiringType;

/**
 *
 * JSON POJO holding data of one contact sensor.
 *
 * @author Milan Ramljak
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "pin", "state", "name", "contactType", "status", "triggerType", "wiringType", "debounceTime",
        "pushDurationTime" })
public class ContactSensor {

    @JsonProperty("id")
    private int id;
    @JsonProperty("pin")
    private int pin;
    @JsonProperty("state")
    private Boolean state;
    @JsonProperty("name")
    private String name;
    @JsonProperty("contactType")
    private ContactType contactType;
    @JsonProperty("status")
    private ContactConnectionType status;
    @JsonProperty("triggerType")
    private ContactTriggerType triggerType;
    @JsonProperty("wiringType")
    private ContactWiringType wiringType = ContactWiringType.Off;
    @JsonProperty("debounceTime")
    private int debounceTime = 100; // milliseconds
    @JsonProperty("pushDurationTime")
    private int pushDurationTime = 1000; // 1 second (1000 milliseconds)
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return The id
     */
    @JsonProperty("id")
    public int getId() {
        return id;
    }

    /**
     *
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return The pin
     */
    @JsonProperty("pin")
    public int getPin() {
        return pin;
    }

    /**
     *
     * @param pin The pin
     */
    @JsonProperty("pin")
    public void setPin(int pin) {
        this.pin = pin;
    }

    /**
     *
     * @return The state
     */
    @JsonProperty("state")
    public Boolean getState() {
        return state;
    }

    /**
     *
     * @param state The initialState
     */
    @JsonProperty("state")
    public void setState(Boolean state) {
        this.state = state;
    }

    /**
     *
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the contactType
     */
    @JsonProperty("contactType")
    public ContactType getContactType() {
        return contactType;
    }

    /**
     * @param contactType the contactType to set
     */
    @JsonProperty("contactType")
    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    /**
     *
     * @return The status
     */
    @JsonProperty("status")
    public ContactConnectionType getStatus() {
        return status;
    }

    /**
     *
     * @param status The status
     */
    @JsonProperty("status")
    public void setStatus(ContactConnectionType status) {
        this.status = status;
    }

    /**
     *
     * @return The triggerType
     */
    @JsonProperty("triggerType")
    public ContactTriggerType getTriggerType() {
        return triggerType;
    }

    /**
     *
     * @param triggerType The triggerType
     */
    @JsonProperty("triggerType")
    public void setTriggerType(ContactTriggerType triggerType) {
        this.triggerType = triggerType;
    }

    /**
     *
     * @return The wiringType
     */
    @JsonProperty("wiringType")
    public ContactWiringType getWiringType() {
        return wiringType;
    }

    /**
     *
     * @param wiringType The wiringType
     */
    @JsonProperty("wiringType")
    public void setWiringType(ContactWiringType wiringType) {
        this.wiringType = wiringType;
    }

    /**
     *
     * @return The debounceTime
     */
    @JsonProperty("debounceTime")
    public int getDebounceTime() {
        return debounceTime;
    }

    /**
     *
     * @param id The debounceTime
     */
    @JsonProperty("debounceTime")
    public void setDebounceTime(int debounceTime) {
        this.debounceTime = debounceTime;
    }

    /**
     *
     * @return The pushDurationTime
     */
    @JsonProperty("pushDurationTime")
    public int getPushDurationTime() {
        return pushDurationTime;
    }

    /**
     *
     * @param id The pushDurationTime
     */
    @JsonProperty("pushDurationTime")
    public void setPushDurationTime(int pushDurationTime) {
        this.pushDurationTime = pushDurationTime;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    /**
     * Helper method to search for custom additional property in contact sensor data set.
     *
     * @param name - key of custom additional property
     * @return value of property or {@code null} if not found
     */
    public String getCustomPropertyByName(String name) {
        String customProperyValue = null;
        if (!getAdditionalProperties().isEmpty() && getAdditionalProperties().containsKey(name)) {
            customProperyValue = (String) getAdditionalProperties().get(name);
        }

        return customProperyValue;
    }

}