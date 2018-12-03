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
 * Object representing relay board connected to thing.
 * 
 * @author Milan Ramljak
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "relayChannels" })
public class RelayBoard {

	@JsonProperty("id")
	private Integer id;
	@JsonProperty("relayChannels")
	private List<RelayChannel> relayChannels = new ArrayList<RelayChannel>();
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
	 * @return The relayChannels
	 */
	@JsonProperty("relayChannels")
	public List<RelayChannel> getRelayChannels() {
		return relayChannels;
	}

	/**
	 * 
	 * @param relayChannels The relayChannels
	 */
	@JsonProperty("relayChannels")
	public void setRelayChannels(List<RelayChannel> relayChannels) {
		this.relayChannels = relayChannels;
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
