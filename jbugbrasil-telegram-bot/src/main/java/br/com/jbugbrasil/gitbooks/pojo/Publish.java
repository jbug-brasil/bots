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
        "defaultBranch",
        "builder"
})
public class Publish {

    @JsonProperty("defaultBranch")
    private String defaultBranch;
    @JsonProperty("builder")
    private Object builder;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The defaultBranch
     */
    @JsonProperty("defaultBranch")
    public String getDefaultBranch() {
        return defaultBranch;
    }

    /**
     *
     * @param defaultBranch
     * The defaultBranch
     */
    @JsonProperty("defaultBranch")
    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    /**
     *
     * @return
     * The builder
     */
    @JsonProperty("builder")
    public Object getBuilder() {
        return builder;
    }

    /**
     *
     * @param builder
     * The builder
     */
    @JsonProperty("builder")
    public void setBuilder(Object builder) {
        this.builder = builder;
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