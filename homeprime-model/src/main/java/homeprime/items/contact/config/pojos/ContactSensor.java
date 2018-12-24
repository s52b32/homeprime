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

/**
 * 
 * JSON POJO holding data of one contact sensor.
 * 
 * @author Milan Ramljak
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "pin", "state", "name", "contactType", "status", "triggerType" })
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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}