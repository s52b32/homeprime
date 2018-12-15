package homeprime.items.relay.config.pojos;

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
 * Object representing relay collection connected to thing.
 * 
 * @author Milan Ramljak
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "relays" })
public class Relays {

	@JsonProperty("relays")
	private List<Relay> relays = new ArrayList<Relay>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The relays
	 */
	@JsonProperty("relays")
	public List<Relay> getRelays() {
		return relays;
	}

	/**
	 * 
	 * @param relays The relays
	 */
	@JsonProperty("relays")
	public void setRelays(List<Relay> relays) {
		this.relays = relays;
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
