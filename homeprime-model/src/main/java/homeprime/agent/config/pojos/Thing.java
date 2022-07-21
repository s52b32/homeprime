
package homeprime.agent.config.pojos;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "uuid", "name", "agent", "management", "maintenance", "active", "version" })
public class Thing {

    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("agent")
    private Agent agent;
    @JsonProperty("management")
    private Management management;
    @JsonProperty("maintenance")
    private Boolean maintenance = false;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("version")
    private String version;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonProperty("uuid")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("agent")
    public Agent getAgent() {
        return agent;
    }

    @JsonProperty("agent")
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    @JsonProperty("management")
    public Management getManagement() {
        return management;
    }

    @JsonProperty("management")
    public void setManagement(Management management) {
        this.management = management;
    }

    @JsonProperty("maintenance")
    public Boolean getMaintenance() {
        return maintenance;
    }

    @JsonProperty("maintenance")
    public void setMaintenance(Boolean maintenance) {
        this.maintenance = maintenance;
    }

    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
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
