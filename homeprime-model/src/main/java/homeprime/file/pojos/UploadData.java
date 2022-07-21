package homeprime.file.pojos;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "destinationDir", "destinationFileName" })
public class UploadData {

    @JsonProperty("destinationDir")
    private String destinationDir;
    @JsonProperty("destinationFileName")
    private String destinationFileName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("destinationDir")
    public String getDestinationDir() {
        return destinationDir;
    }

    @JsonProperty("destinationDir")
    public void setDestinationDir(String destinationDir) {
        this.destinationDir = destinationDir;
    }

    @JsonProperty("destinationFileName")
    public String getDestinationFileName() {
        return destinationFileName;
    }

    @JsonProperty("destinationFileName")
    public void setDestinationFileName(String destinationFileName) {
        this.destinationFileName = destinationFileName;
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