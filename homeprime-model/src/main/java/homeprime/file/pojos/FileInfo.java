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
@JsonPropertyOrder({ "fileName", "filePath", "fileSize", "creationDate" })
public class FileInfo {

    @JsonProperty("fileName")
    private String fileName;
    @JsonProperty("filePath")
    private String filePath;
    @JsonProperty("fileSize")
    private Long fileSize;
    @JsonProperty("creationDate")
    private Long creationDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("fileName")
    public String getFileName() {
        return fileName;
    }

    @JsonProperty("fileName")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonProperty("filePath")
    public String getFilePath() {
        return filePath;
    }

    @JsonProperty("filePath")
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Get image size in B.
     *
     * @return
     */
    @JsonProperty("fileSize")
    public Long getFileSize() {
        return fileSize;
    }

    /**
     * Set image size in B.
     *
     * @param fileSize
     */
    @JsonProperty("fileSize")
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @JsonProperty("creationDate")
    public Long getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creationDate")
    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
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