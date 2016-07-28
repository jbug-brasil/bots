package br.com.jbugbrasil.commands.faq;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Indexed
public class Project {

    @JsonProperty("id")
    @Field(store = Store.YES, analyze = Analyze.NO)
    public String id;
    @JsonProperty("link")
    @Field(store = Store.YES, analyze = Analyze.NO)
    public String link;
    @JsonProperty("description")
    @Field(store = Store.YES, analyze = Analyze.NO)
    public String description;

    public Project() {
    }

    /*
    * Returns the project name with the project's link in the markdown pattern
    */
    @Override
    public String toString() {
        return "[" + getId() + "](" + getLink() + ")";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}