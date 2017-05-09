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
        "edit",
        "admin"
})
public class Permissions_ {

    @JsonProperty("edit")
    private Object edit;
    @JsonProperty("admin")
    private Object admin;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The edit
     */
    @JsonProperty("edit")
    public Object getEdit() {
        return edit;
    }

    /**
     *
     * @param edit
     * The edit
     */
    @JsonProperty("edit")
    public void setEdit(Object edit) {
        this.edit = edit;
    }

    /**
     *
     * @return
     * The admin
     */
    @JsonProperty("admin")
    public Object getAdmin() {
        return admin;
    }

    /**
     *
     * @param admin
     * The admin
     */
    @JsonProperty("admin")
    public void setAdmin(Object admin) {
        this.admin = admin;
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