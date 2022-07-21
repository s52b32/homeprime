package homeprime.manager.pojos;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import homeprime.manager.pojos.enums.BackupType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "type", "name", "app-version", "timestamp" })
@Generated("jsonschema2pojo")
public class BackupFile {

    @JsonProperty("type")
    private BackupType type = BackupType.Unknown;
    @JsonProperty("name")
    private String name;
    @JsonProperty("app-version")
    private String appVersion;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type")
    public BackupType getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(BackupType type) {
        this.type = type;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("app-version")
    public String getAppVersion() {
        return appVersion;
    }

    @JsonProperty("app-version")
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @JsonProperty("timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
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
