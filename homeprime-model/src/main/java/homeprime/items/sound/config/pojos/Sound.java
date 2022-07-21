package homeprime.items.sound.config.pojos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "streams", "speakers" })
public class Sound {

    @JsonProperty("streams")
    private List<Stream> streams = null;
    @JsonProperty("speakers")
    private List<Speaker> speakers = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("streams")
    public List<Stream> getStreams() {
        return streams;
    }

    @JsonProperty("streams")
    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }

    @JsonProperty("speakers")
    public List<Speaker> getSpeakers() {
        return speakers;
    }

    @JsonProperty("speakers")
    public void setSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
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