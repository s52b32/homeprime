package homeprime.items.contact.config.pojos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * JSON POJO holding list of contact sensor POJOs.
 * 
 * @author Milan Ramljak
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "contacts" })
public class ContactSensors {

	@JsonProperty("contacts")
	private List<ContactSensor> contacts = new ArrayList<ContactSensor>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The contacts
	 */
	@JsonProperty("contacts")
	public List<ContactSensor> getContacts() {
		return contacts;
	}

	/**
	 * 
	 * @param contacts The contacts
	 */
	@JsonProperty("contacts")
	public void setContacts(List<ContactSensor> contacts) {
		this.contacts = contacts;
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