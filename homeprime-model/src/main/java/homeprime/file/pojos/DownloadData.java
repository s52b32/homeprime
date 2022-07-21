package homeprime.file.pojos;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "destinationPath", "destinationFileName" })
public class DownloadData {

    @JsonProperty("destinationPath")
    private String destinationPath;
    @JsonProperty("destinationFileName")
    private String destinationFileName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("destinationPath")
    public String getDestinationPath() {
        return destinationPath;
    }

    @JsonProperty("destinationPath")
    public void setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
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

    public String getAbsoluteFilePath() {
        String fullPath = "";
        if (destinationPath != null) {
            fullPath = destinationPath;
        }
        if (destinationFileName != null) {
            if (fullPath.endsWith(File.separator)) {
                fullPath = fullPath + destinationFileName;
            } else {
                fullPath = fullPath + File.separator + destinationFileName;
            }
        }
        return fullPath;
    }

}