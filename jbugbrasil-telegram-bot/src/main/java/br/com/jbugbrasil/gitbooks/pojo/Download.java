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
        "epub",
        "mobi",
        "pdf"
})
public class Download {

    @JsonProperty("epub")
    private String epub;
    @JsonProperty("mobi")
    private String mobi;
    @JsonProperty("pdf")
    private String pdf;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The epub
     */
    @JsonProperty("epub")
    public String getEpub() {
        return epub;
    }

    /**
     *
     * @param epub
     * The epub
     */
    @JsonProperty("epub")
    public void setEpub(String epub) {
        this.epub = epub;
    }

    /**
     *
     * @return
     * The mobi
     */
    @JsonProperty("mobi")
    public String getMobi() {
        return mobi;
    }

    /**
     *
     * @param mobi
     * The mobi
     */
    @JsonProperty("mobi")
    public void setMobi(String mobi) {
        this.mobi = mobi;
    }

    /**
     *
     * @return
     * The pdf
     */
    @JsonProperty("pdf")
    public String getPdf() {
        return pdf;
    }

    /**
     *
     * @param pdf
     * The pdf
     */
    @JsonProperty("pdf")
    public void setPdf(String pdf) {
        this.pdf = pdf;
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