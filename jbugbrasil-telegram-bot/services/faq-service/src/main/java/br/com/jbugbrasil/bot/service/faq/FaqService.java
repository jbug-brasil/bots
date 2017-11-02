/*
 The MIT License (MIT)

 Copyright (c) 2017 JBug:Brasil <contato@jbugbrasil.com.br>

 Permission is hereby granted, free of charge, to any person obtaining a copy of
 this software and associated documentation files (the "Software"), to deal in
 the Software without restriction, including without limitation the rights to
 use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 the Software, and to permit persons to whom the Software is furnished to do so,
 subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package br.com.jbugbrasil.bot.service.faq;

import br.com.jbugbrasil.bot.api.emojis.Emoji;
import br.com.jbugbrasil.bot.service.cache.qualifier.FaqCache;
import br.com.jbugbrasil.bot.service.faq.pojo.Project;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.infinispan.Cache;

import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
@LocalBean
public class FaqService {

    public static final String JSON_SOURCE_LOCATION = "https://raw.githubusercontent.com/jbug-brasil/bots/master/jbugbrasil-telegram-bot/services/faq-service/src/main/resources/META-INF/faq-properties.json";

    private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final String PROJECT_NOT_FOUND_MESSAGE = "Ooops, não encontrei nenhum projeto com o nome <b>%s</b>. " + Emoji.DISAPPOINTED_FACE;

    @Inject
    @FaqCache(classToIndex = "br.com.jbugbrasil.bot.faqservice.service.pojo.Project")
    private Cache<String, Project> cache;


    @Schedule(minute = "*/30", hour = "*", persistent = false)
    public void populateCache() {
        try {
            log.fine("Tentando ler o arquivo " + JSON_SOURCE_LOCATION + " do github");
            URL jsonUrl = new URL(JSON_SOURCE_LOCATION);

            ObjectMapper mapper = new ObjectMapper();
            List<Project> myObjects = mapper.readValue(jsonUrl, new TypeReference<List<Project>>() {
            });

            myObjects.stream().forEach(project -> {
                log.fine("Adicionando objeto no cache: [" + project.getId() + " - " + project.id + "]");
                cache.putIfAbsent(project.getId(), project);
            });
            log.fine("Cache populado com sucesso.");
            myObjects.clear();
        } catch (Exception e) {
            log.warning("Falha ao popular o cache: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param key termo para ser pesquisado
     * @return retorna o resultado, caso não seja encontrado uma mensagem é retornada informando que o projeto procurado não foi encontrado.
     */
    public String query(String key) {
        StringBuilder stbuilder = new StringBuilder();

        // get all Project type items from cache
        List<Project> cacheEntries = cache.values().stream()
                .filter(item -> item instanceof Project)
                .collect(Collectors.toList());

        // Filter all elements that matches the given key and append it to the response
        for (Project project : cacheEntries.stream().filter(item -> item.getId().contains(key)).collect(Collectors.toList())) {
            stbuilder.append(project.toString());
            stbuilder.append(" - ");
            stbuilder.append(project.getDescription() + "\n");
        }

        if (stbuilder.length() <= 0) {
            stbuilder.append(String.format(PROJECT_NOT_FOUND_MESSAGE, key));
        }

        //stbuilder.append("\n\nProvider: <a href=\"" + BotConfig.JSON_SOURCE_LOCATION + "\">" + this.name() + "</a>");
        return stbuilder.toString();
    }

}
