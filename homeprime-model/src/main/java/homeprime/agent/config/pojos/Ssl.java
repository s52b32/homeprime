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
@JsonPropertyOrder({ "key-store-type", "key-store", "key-store-password", "key-alias" })
public class Ssl {

    @JsonProperty("key-store-type")
    private String keyStoreType;
    @JsonProperty("key-store")
    private String keyStore;
    @JsonProperty("key-store-password")
    private String keyStorePassword;
    @JsonProperty("key-alias")
    private String keyAlias;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("key-store-type")
    public String getKeyStoreType() {
        return keyStoreType;
    }

    @JsonProperty("key-store-type")
    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    @JsonProperty("key-store")
    public String getKeyStore() {
        return keyStore;
    }

    @JsonProperty("key-store")
    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    @JsonProperty("key-store-password")
    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    @JsonProperty("key-store-password")
    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    @JsonProperty("key-alias")
    public String getKeyAlias() {
        return keyAlias;
    }

    @JsonProperty("key-alias")
    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
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