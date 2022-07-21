package homeprime.manager.pojos;

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
@JsonPropertyOrder({ "thing-uuid", "files" })
@Generated("jsonschema2pojo")
public class BackupFiles {

    @JsonProperty("thing-uuid")
    private String thingUuid;
    @JsonProperty("files")
    private List<BackupFile> files = new ArrayList<BackupFile>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("thing-uuid")
    public String getThingUuid() {
        return thingUuid;
    }

    @JsonProperty("thing-uuid")
    public void setThingUuid(String thingUuid) {
        this.thingUuid = thingUuid;
    }

    @JsonProperty("files")
    public List<BackupFile> getFiles() {
        return files;
    }

    @JsonProperty("files")
    public void setFiles(List<BackupFile> files) {
        this.files = files;
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