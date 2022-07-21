package homeprime.manager.tasks.pojos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import homeprime.manager.tasks.pojos.enums.TaskStatusType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "uuid", "creationDate", "owner", "status", "message", "description" })
public class Task {

    @JsonProperty("uuid")
    private String uuid = UUID.randomUUID().toString();
    @JsonProperty("owner")
    private String owner;
    @JsonProperty("status")
    private TaskStatusType status = TaskStatusType.New;
    @JsonProperty("message")
    private String message;
    @JsonProperty("description")
    private String description;
    @JsonProperty("creationDate")
    private Long creationDate = System.currentTimeMillis();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Task(String owner) {
        this.owner = owner;
    }

    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonProperty("uuid")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @JsonProperty("owner")
    public String getOwner() {
        return owner;
    }

    @JsonProperty("owner")
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @JsonProperty("status")
    public TaskStatusType getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(TaskStatusType status) {
        this.status = status;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
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