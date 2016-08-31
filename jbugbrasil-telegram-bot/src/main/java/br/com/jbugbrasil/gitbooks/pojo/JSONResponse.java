package br.com.jbugbrasil.gitbooks.pojo;
import java.util.ArrayList;
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
        "list",
        "total",
        "limit",
        "page",
        "pages"
})
public class JSONResponse {

    @JsonProperty("list")
    private java.util.List<Books> list = new ArrayList<Books>();
    @JsonProperty("total")
    private int total;
    @JsonProperty("limit")
    private int limit;
    @JsonProperty("page")
    private int page;
    @JsonProperty("pages")
    private int pages;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The list
     */
    @JsonProperty("list")
    public java.util.List<Books> getList() {
        return list;
    }

    /**
     *
     * @param list
     * The list
     */
    @JsonProperty("list")
    public void setList(java.util.List<Books> list) {
        this.list = list;
    }

    /**
     *
     * @return
     * The total
     */
    @JsonProperty("total")
    public int getTotal() {
        return total;
    }

    /**
     *
     * @param total
     * The total
     */
    @JsonProperty("total")
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     *
     * @return
     * The limit
     */
    @JsonProperty("limit")
    public int getLimit() {
        return limit;
    }

    /**
     *
     * @param limit
     * The limit
     */
    @JsonProperty("limit")
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     *
     * @return
     * The page
     */
    @JsonProperty("page")
    public int getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    @JsonProperty("page")
    public void setPage(int page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The pages
     */
    @JsonProperty("pages")
    public int getPages() {
        return pages;
    }

    /**
     *
     * @param pages
     * The pages
     */
    @JsonProperty("pages")
    public void setPages(int pages) {
        this.pages = pages;
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