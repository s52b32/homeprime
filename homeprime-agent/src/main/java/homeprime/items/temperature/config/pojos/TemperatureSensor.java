package homeprime.items.temperature.config.pojos;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import homeprime.items.temperature.config.enums.TemperatureSensorType;
import homeprime.items.temperature.config.enums.TemperatureValueType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "id", "i2cBus", "i2cAddress", "name", "sensorType", "sensorValueType" })
public class TemperatureSensor {

    @JsonProperty("id")
    private int id;
    @JsonProperty("i2cBus")
    private int i2cBus;
    @JsonProperty("i2cAddress")
    private String i2cAddress;
    @JsonProperty("name")
    private String name;
    @JsonProperty("sensorType")
    private TemperatureSensorType sensorType;
    @JsonProperty("sensorValueType")
    private TemperatureValueType sensorValueType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return The id
     */
    @JsonProperty("id")
    public int getId() {
	return id;
    }

    /**
     * 
     * @param id
     *            The id
     */
    @JsonProperty("id")
    public void setId(int id) {
	this.id = id;
    }

    /**
     * 
     * @return The i2cBus
     */
    @JsonProperty("i2cBus")
    public int getI2cBus() {
	return i2cBus;
    }

    /**
     * 
     * @param i2cBus
     *            The i2cBus
     */
    @JsonProperty("i2cBus")
    public void setI2cBus(int i2cBus) {
	this.i2cBus = i2cBus;
    }

    /**
     * 
     * @return The i2cAddress
     */
    @JsonProperty("i2cAddress")
    public String getI2cAddress() {
	return i2cAddress;
    }

    /**
     * 
     * @param i2cAddress
     *            The i2cAddress
     */
    @JsonProperty("i2cAddress")
    public void setI2cAddress(String i2cAddress) {
	this.i2cAddress = i2cAddress;
    }

    /**
     * 
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
	return name;
    }

    /**
     * 
     * @param name
     *            The name
     */
    @JsonProperty("name")
    public void setName(String name) {
	this.name = name;
    }

    /**
     * 
     * @return The sensorType
     */
    @JsonProperty("sensorType")
    public TemperatureSensorType getSensorType() {
	return sensorType;
    }

    /**
     * 
     * @param sensorType
     *            The sensorType
     */
    @JsonProperty("sensorType")
    public void setSensorType(TemperatureSensorType sensorType) {
	this.sensorType = sensorType;
    }

    /**
     * 
     * @return The sensorValueType
     */
    @JsonProperty("sensorValueType")
    public TemperatureValueType getSensorValueType() {
	return sensorValueType;
    }

    /**
     * 
     * @param sensorValueType
     *            The sensorValueType
     */
    @JsonProperty("sensorValueType")
    public void setSensorValueType(TemperatureValueType sensorValueType) {
	this.sensorValueType = sensorValueType;
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