package homeprime.agent.config.pojos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "things" })
public class Things {

    @JsonProperty("things")
    private List<Thing> things = null;

    @JsonProperty("things")
    public List<Thing> getThings() {
        return things;
    }

    @JsonProperty("things")
    public void setThings(List<Thing> things) {
        this.things = things;
    }

}