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
        "admin",
        "important"
})
public class Permissions {

    @JsonProperty("edit")
    private boolean edit;
    @JsonProperty("admin")
    private boolean admin;
    @JsonProperty("important")
    private boolean important;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The edit
     */
    @JsonProperty("edit")
    public boolean isEdit() {
        return edit;
    }

    /**
     *
     * @param edit
     * The edit
     */
    @JsonProperty("edit")
    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    /**
     *
     * @return
     * The admin
     */
    @JsonProperty("admin")
    public boolean isAdmin() {
        return admin;
    }

    /**
     *
     * @param admin
     * The admin
     */
    @JsonProperty("admin")
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     *
     * @return
     * The important
     */
    @JsonProperty("important")
    public boolean isImportant() {
        return important;
    }

    /**
     *
     * @param important
     * The important
     */
    @JsonProperty("important")
    public void setImportant(boolean important) {
        this.important = important;
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