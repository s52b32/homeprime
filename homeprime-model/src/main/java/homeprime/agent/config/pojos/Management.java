
package homeprime.agent.config.pojos;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import homeprime.agent.config.enums.ManagementAppType;
import homeprime.agent.config.enums.ManagementMode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "ip", "port", "name", "mode", "type", "appType" })
public class Management {

    @JsonProperty("ip")
    private String ip = "127.0.0.1";
    @JsonProperty("port")
    private Integer port = 8080;
    @JsonProperty("name")
    private String name = "default";
    @JsonProperty("mode")
    private ManagementMode mode = ManagementMode.Pull;
    @JsonProperty("appType")
    private ManagementAppType appType = ManagementAppType.OpenHAB;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ip")
    public String getIp() {
        return ip;
    }

    @JsonProperty("ip")
    public void setIp(String ip) {
        this.ip = ip;
    }

    @JsonProperty("port")
    public Integer getPort() {
        return port;
    }

    @JsonProperty("port")
    public void setPort(Integer port) {
        this.port = port;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("mode")
    public ManagementMode getManagementMode() {
        return mode;
    }

    @JsonProperty("mode")
    public void setManagementMode(ManagementMode mode) {
        this.mode = mode;
    }

    @JsonProperty("appType")
    public ManagementAppType getAppType() {
        return appType;
    }

    @JsonProperty("appType")
    public void setAppType(ManagementAppType appType) {
        this.appType = appType;
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
