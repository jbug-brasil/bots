package br.com.jbugbrasil.commands.urbandictionary;

import br.com.jbugbrasil.cache.CacheProviderImpl;
import br.com.jbugbrasil.commands.Commands;
import br.com.jbugbrasil.urbandictionary.client.UrbanDictionaryClient;
import br.com.jbugbrasil.urbandictionary.client.builder.UrbanDictionaryClientBuilder;
import br.com.jbugbrasil.urbandictionary.client.helper.CustomTermResponse;
import br.com.jbugbrasil.utils.Utils;
import br.com.jbugbrasil.utils.message.impl.MessageSender;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by fspolti on 5/15/17.
 */
public class UrbanDictionaryCommand extends BotCommand implements Commands {

    private Logger log = Logger.getLogger(UrbanDictionaryCommand.class.getName());

    private SendMessage ud = new SendMessage();
    MessageSender msg = new MessageSender(ud);

    private final ICommandRegistry commandRegistry;
    private static final CacheProviderImpl cache = CacheProviderImpl.getInstance();

    public UrbanDictionaryCommand(ICommandRegistry commandRegistry) {
        super(Commands.URBAN_DICTIONARY, "Pesquisa por um termo/gíria em inglês. -c N seta o número de resultados, -e adiciona um exemplo de como usar a gíria. Examplo: /define -c 2 -e lol");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] parameters) {
        String term = "";
        int numberOfResults = 1;
        boolean showExample = false;
        ud.setChatId(chat.getId().toString());
        ud.enableHtml(true);

        if (parameters.length == 0) {
            ud.setText("<b>/define</b>\nParametro é obrigatório");
            msg.send();
            throw new InvalidParameterException("O comando /define requer um parâmetro.");
        }
        // prepare the parameters
        for (int i = 0; i < parameters.length; i++) {

            if (parameters[i].equals("-c")) {
                i += 1;
                try {
                    numberOfResults = Integer.parseInt(parameters[i]);
                } catch (NumberFormatException e) {

                    ud.setText("Parametro " + parameters[i] + " não é válido. Para maiores detalhes utilize o comando help.");
                    msg.send();
                    throw new NumberFormatException("Parametro " + parameters[i] + " não é válido. Para maiores detalhes utilize o comando help.");
                }

            } else if (parameters[i].equals("-e")) {
                showExample = true;
            } else {
                if (parameters.length > i + 1) {
                    term += parameters[i] + " ";
                } else {
                    term += parameters[i];
                }
            }
        }

        List<CustomTermResponse> ubResponse = new ArrayList<>();
        try {
            ubResponse = processRequest(term, numberOfResults, showExample);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder response = new StringBuilder("<b>" + Utils.prepareString(term) + "</b>\n");

        if (ubResponse.size() == 0) {
            response.append("Termo não encontrado");
        } else {
            ubResponse.stream().forEach(item -> {
                response.append("<b>Definition:</b> " + prepareTerm(item.getDefinition()));
                if (item.getExample() != null) {
                    response.append("\n<b>Example:</b> " + prepareTerm(item.getExample()));
                }
                response.append("\n+++++++++++++++++++++++++\n");
            });
        }
        ud.setText(response.toString());
        msg.send();
    }

    private String prepareTerm (String term) {
        return term.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }

    /**
     * Make a new request againts urban dictionary api.
     * for every request verify if the therm is on cache, if not, make a new request and put it on the cache.
     * Also, if the entry on cache have only 1 definition, but user has requested for more than 1, make a new
     * request and update the cache entry.
     *
     * @param term
     * @param numberOfResults
     * @param showExample
     * @return {@link List<CustomTermResponse>}
     */
    private List<CustomTermResponse> processRequest(String term, int numberOfResults, boolean showExample) throws UnsupportedEncodingException {
        UrbanDictionaryClient client;
        if (cache.getCache().containsKey("ub" + term.trim())) {
            List<CustomTermResponse> cacheItens = (List<CustomTermResponse>) cache.getCache().get("ub" + term.trim());
            if (showExample || cacheItens.size() != numberOfResults) {
                List<CustomTermResponse> itensWithNoExample = cacheItens.stream().filter(item -> item.getExample() == null).collect(Collectors.toList());
                if (itensWithNoExample.size() > 0 || cacheItens.size() != numberOfResults) {
                    log.fine(term + " is available on cache but some information is missing.");
                    if (showExample)
                        client = new UrbanDictionaryClientBuilder().term(term).numberOfResults(numberOfResults).showExample().build();
                    else
                        client = new UrbanDictionaryClientBuilder().term(term).numberOfResults(numberOfResults).build();
                    List<CustomTermResponse> ubResult = client.execute();
                    cache.getCache().replace("ub" + term.trim(), ubResult, 1, TimeUnit.DAYS);
                    return ubResult;
                }
            } else {
                log.fine(term + " is available on cache, returning it from cache.");
                return cacheItens;
            }
        }
        log.fine(term + " is not available on cache, making a new request.");
        if (showExample)
            client = new UrbanDictionaryClientBuilder().term(term).numberOfResults(numberOfResults).showExample().build();
        else client = new UrbanDictionaryClientBuilder().term(term).numberOfResults(numberOfResults).build();
        List<CustomTermResponse> ubResult = client.execute();
        cache.getCache().putIfAbsent("ub" + term.trim(), ubResult, 1, TimeUnit.DAYS);
        return ubResult;
    }
}