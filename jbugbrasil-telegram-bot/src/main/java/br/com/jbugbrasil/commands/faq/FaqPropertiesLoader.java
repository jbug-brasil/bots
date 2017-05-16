package br.com.jbugbrasil.commands.faq;

import br.com.jbugbrasil.Component;
import br.com.jbugbrasil.cache.CacheProviderImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class FaqPropertiesLoader implements Component {

    private final Logger log = Logger.getLogger(FaqPropertiesLoader.class.getName());

    private static final String FILE_NAME = "faq-properties.json";
    private static final String FAQ_PROPERTIES = "https://raw.githubusercontent.com/jbug-brasil/bots/master/jbugbrasil-telegram-bot/src/main/resources/META-INF/faq-properties.json";

    private CacheProviderImpl cache = CacheProviderImpl.getInstance();

    //Read the projects from json file then send it to the cache.
    @Override
    public void initialize() {
        try {
            log.info("Tentando ler o arquivo " + FILE_NAME + " do github");
            URL jsonUrl = new URL(FAQ_PROPERTIES);

            ObjectMapper mapper = new ObjectMapper();
            List<Project> myObjects = mapper.readValue(jsonUrl, new TypeReference<List<Project>>() {
            });

            //Put everything in the cache
            for (Project project : myObjects) {
                cache.getCache().putIfAbsent(project.getId(), project);
            }

            log.info("Cache populado com sucesso.");
            myObjects.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}