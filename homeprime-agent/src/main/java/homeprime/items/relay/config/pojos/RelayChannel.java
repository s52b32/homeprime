package homeprime.items.relay.config.pojos;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import homeprime.items.relay.config.enums.PayloadType;
import homeprime.items.relay.config.enums.RelayType;

/**
 * 
 * @author Milan Ramljak
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "pin", "name", "relayType", "payloadType" })
public class RelayChannel {

	@JsonProperty("id")
	private Integer id;
	@JsonProperty("pin")
	private Integer pin;
	@JsonProperty("name")
	private String name;
	@JsonProperty("relayType")
	private RelayType relayType = RelayType.NO;
	@JsonProperty("payloadType")
	private PayloadType payloadType = PayloadType.Unknown;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The id
	 */
	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id The id
	 */
	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The pin
	 */
	@JsonProperty("pin")
	public Integer getPin() {
		return pin;
	}

	/**
	 * 
	 * @param pin The pin
	 */
	@JsonProperty("pin")
	public void setPin(Integer pin) {
		this.pin = pin;
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
	 * Get relay type. Default value is set to 0 -> NO (normally-opened)
	 * 
	 * @return The relayType
	 */
	@JsonProperty("relayType")
	public RelayType getRelayType() {
		return relayType;
	}

	/**
	 * 
	 * @param relayType The relayType
	 */
	@JsonProperty("relayType")
	public void setRelayType(RelayType relayType) {
		this.relayType = relayType;
	}

	/**
	 * 
	 * @return The payloadType
	 */
	@JsonProperty("payloadType")
	public PayloadType getPayloadType() {
		return payloadType;
	}

	/**
	 * 
	 * @param payloadType The payloadType
	 */
	@JsonProperty("payloadType")
	public void setPayloadType(PayloadType payloadType) {
		this.payloadType = payloadType;
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
