package homeprime.items.sound.tts.config.pojos;

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
@JsonPropertyOrder({ "tts_configs" })
public class TtsConfigs {

    @JsonProperty("tts_configs")
    private List<TtsConfig> ttsConfigs = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("tts_configs")
    public List<TtsConfig> getTtsConfigs() {
        return ttsConfigs;
    }

    @JsonProperty("tts_configs")
    public void setTtsConfigs(List<TtsConfig> ttsConfigs) {
        this.ttsConfigs = ttsConfigs;
    }

    /**
     * Get enabled TTS.
     *
     * @return {@code null} if no TTS is enabled.
     */
    public TtsConfig getEnabledTtsConfig() {
        if (ttsConfigs != null) {
            for (TtsConfig ttsConfig : ttsConfigs) {
                if (ttsConfig.getEnabled()) {
                    return ttsConfig;
                }
            }
        }
        return null;
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