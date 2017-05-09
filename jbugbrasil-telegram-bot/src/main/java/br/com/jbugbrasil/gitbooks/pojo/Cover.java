package br.com.jbugbrasil.gitbooks.pojo;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "large",
        "small"
})
public class Cover {

    @JsonProperty("large")
    private String large;
    @JsonProperty("small")
    private String small;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The large
     */
    @JsonProperty("large")
    public String getLarge() {
        return large;
    }

    /**
     *
     * @param large
     * The large
     */
    @JsonProperty("large")
    public void setLarge(String large) {
        this.large = large;
    }

    /**
     *
     * @return
     * The small
     */
    @JsonProperty("small")
    public String getSmall() {
        return small;
    }

    /**
     *
     * @param small
     * The small
     */
    @JsonProperty("small")
    public void setSmall(String small) {
        this.small = small;
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