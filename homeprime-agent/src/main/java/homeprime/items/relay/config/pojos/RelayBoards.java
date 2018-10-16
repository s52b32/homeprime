package homeprime.items.relay.config.pojos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "relayBoards" })
public class RelayBoards {

    @JsonProperty("relayBoards")
    private List<RelayBoard> relayBoards = new ArrayList<RelayBoard>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return The relayBoards
     */
    @JsonProperty("relayBoards")
    public List<RelayBoard> getRelayBoards() {
	return relayBoards;
    }

    /**
     * 
     * @param relayBoards
     *            The relayBoards
     */
    @JsonProperty("relayBoards")
    public void setRelayBoards(List<RelayBoard> relayBoards) {
	this.relayBoards = relayBoards;
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