package br.com.jbugbrasil.commands.faq;

import br.com.jbugbrasil.Main;
import br.com.jbugbrasil.cache.CacheProviderImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class FaqPropertiesLoader {

    private static final Logger log = Logger.getLogger(FaqPropertiesLoader.class.getName());

    private static final String FILE_LOCATION = "/META-INF/faq-properties.json";

    private static final CacheProviderImpl cache = CacheProviderImpl.getInstance();
    private static InputStream input = null;

    /*
    * Static loader
    */
    static {
        try {
            log.info("Tentando ler o arquivo " + FILE_LOCATION);
            input = Main.class.getClass().getResourceAsStream(FILE_LOCATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Load the file faq-properties.json from classpath, unmarshall and put it in the cache.
    */
    public static void load() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        List<Project> myObjects = mapper.readValue(input, new TypeReference<List<Project>>() {});

        //Put everything in the cache
        for (Project project : myObjects) {
            cache.getCache().put(project.getId(), project);
        }
        log.info("Cache populado com sucesso.");
        myObjects.clear();
    }
}