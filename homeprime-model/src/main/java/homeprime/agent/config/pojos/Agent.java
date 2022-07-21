
package homeprime.agent.config.pojos;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import homeprime.agent.config.enums.LoggerType;
import homeprime.agent.config.enums.SystemType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "type", "ip", "port", "mode", "ssl", "loggerType", "loggerFilePath" })
public class Agent {

    @JsonProperty("type")
    private SystemType type = SystemType.Mock;
    @JsonProperty("ip")
    private String ip = "127.0.0.1";
    @JsonProperty("port")
    private Integer port = 8081;
    @JsonProperty("mode")
    private String mode = "PULL";
    @JsonProperty("ssl")
    private Ssl ssl;
    @JsonProperty("loggerType")
    private LoggerType loggerType = LoggerType.Void;
    @JsonProperty("loggerFilePath")
    private String loggerFilePath = "/tmp/IoTLogger.log";
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type")
    public SystemType getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(SystemType type) {
        this.type = type;
    }

    @JsonProperty("ip")
    public String getIp() {
        return ip;
    }

    @JsonProperty("ip")
    public void setPort(String ip) {
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

    @JsonProperty("mode")
    public String getMode() {
        return mode;
    }

    @JsonProperty("mode")
    public void setMode(String mode) {
        this.mode = mode;
    }

    @JsonProperty("ssl")
    public Ssl getSsl() {
        return ssl;
    }

    @JsonProperty("ssl")
    public void setSsl(Ssl ssl) {
        this.ssl = ssl;
    }

    @JsonProperty("loggerType")
    public LoggerType getLoggerType() {
        return loggerType;
    }

    @JsonProperty("loggerType")
    public void setType(LoggerType loggerType) {
        this.loggerType = loggerType;
    }

    @JsonProperty("loggerFilePath")
    public String getLoggerFilePath() {
        return loggerFilePath;
    }

    @JsonProperty("loggerFilePath")
    public void setLoggerFilePath(String loggerFilePath) {
        this.loggerFilePath = loggerFilePath;
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
