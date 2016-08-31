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
        "git",
        "access",
        "homepage",
        "read",
        "edit",
        "content",
        "download"
})
public class Urls {

    @JsonProperty("git")
    private String git;
    @JsonProperty("access")
    private String access;
    @JsonProperty("homepage")
    private String homepage;
    @JsonProperty("read")
    private String read;
    @JsonProperty("edit")
    private String edit;
    @JsonProperty("content")
    private String content;
    @JsonProperty("download")
    private Download download;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The git
     */
    @JsonProperty("git")
    public String getGit() {
        return git;
    }

    /**
     *
     * @param git
     * The git
     */
    @JsonProperty("git")
    public void setGit(String git) {
        this.git = git;
    }

    /**
     *
     * @return
     * The access
     */
    @JsonProperty("access")
    public String getAccess() {
        return access;
    }

    /**
     *
     * @param access
     * The access
     */
    @JsonProperty("access")
    public void setAccess(String access) {
        this.access = access;
    }

    /**
     *
     * @return
     * The homepage
     */
    @JsonProperty("homepage")
    public String getHomepage() {
        return homepage;
    }

    /**
     *
     * @param homepage
     * The homepage
     */
    @JsonProperty("homepage")
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     *
     * @return
     * The read
     */
    @JsonProperty("read")
    public String getRead() {
        return read;
    }

    /**
     *
     * @param read
     * The read
     */
    @JsonProperty("read")
    public void setRead(String read) {
        this.read = read;
    }

    /**
     *
     * @return
     * The edit
     */
    @JsonProperty("edit")
    public String getEdit() {
        return edit;
    }

    /**
     *
     * @param edit
     * The edit
     */
    @JsonProperty("edit")
    public void setEdit(String edit) {
        this.edit = edit;
    }

    /**
     *
     * @return
     * The content
     */
    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    /**
     *
     * @param content
     * The content
     */
    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    /**
     *
     * @return
     * The download
     */
    @JsonProperty("download")
    public Download getDownload() {
        return download;
    }

    /**
     *
     * @param download
     * The download
     */
    @JsonProperty("download")
    public void setDownload(Download download) {
        this.download = download;
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