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

package br.com.jbugbrasil.bot.plugin.karma;

import br.com.jbugbrasil.bot.api.emojis.Emoji;
import br.com.jbugbrasil.bot.api.object.MessageUpdate;
import br.com.jbugbrasil.bot.api.spi.PluginProvider;
import br.com.jbugbrasil.bot.plugin.karma.listener.KarmaEventListener;
import br.com.jbugbrasil.bot.service.cache.qualifier.KarmaCache;
import br.com.jbugbrasil.bot.service.persistence.repository.KarmaRepository;
import org.infinispan.Cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@ApplicationScoped
public class KarmaPluginProvider implements PluginProvider {

    private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private final Pattern FULL_MSG_PATTERN = Pattern.compile("(\\w*)(\\+\\+|\\-\\-)(\\s|$)");
    private final Pattern KARMA_PATTERN = Pattern.compile("(^\\S+)(\\+\\+|\\-\\-)($)");
    private final String KARMA_MESSAGE = "<b>%s</b> tem <b>%d</b> pontos de karma\n";

    @Inject
    @KarmaCache
    private Cache<String, Integer> cache;

    @Inject
    private KarmaEventListener karmaEventListener;

    @Inject
    private KarmaRepository karma;

    @Override
    public void load() {
        cache.start();
        cache.addListener(karmaEventListener);
        log.fine("Plugin karma-plugin ativado.");
    }

    @Override
    public String process(MessageUpdate update) {
        StringBuilder response = new StringBuilder();
        try {
            if (canProcess(update.getMessage().getText())) {
                List<String> itens = Arrays.asList(update.getMessage().getText().split(" "));
                HashMap<String, String> finalTargets = new HashMap<>();
                // Obtem o username do usuário que está alterando o karma para não ser possível ele alterar seu próprio karma.
                String username = update.getMessage().getFrom().getUsername() != null ? update.getMessage().getFrom().getUsername() : update.getMessage().getFrom().getFirstName().toLowerCase();
                itens.stream().distinct().forEach(item -> {
                    if ((KARMA_PATTERN.matcher(item).find())) {
                        finalTargets.putIfAbsent(item.substring(0, item.length() - 2).toLowerCase(), item.substring(item.length() - 2));
                    }
                });

                for (Map.Entry<String, String> entry : finalTargets.entrySet()) {
                    response.append(processKarma(entry.getValue(), entry.getKey(), username));
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            log.warning(e.getMessage());
        }
        return response.toString();
    }

    /**
     * Processa o karma solicitado pelo usuário. Para disparar o karma é necessário utilizar ++ ou -- no final de uma palavra.
     *
     * @param operator ++ ou --
     * @param target   usuário cou alguma palavra que terá seu karma alterado
     * @param username usuário que solicitou a alteração de karma
     * @return o karma atual + ou - 1 ou returna null em caso de karma excessivo
     */
    private String processKarma(String operator, String target, String username) {

        // não é permitido um usuário alterar seu próprio karma
        if (target.equals(username)) {
            return "Ooops, querendo alterar seu próprio karma? " + Emoji.DIZZY_FACE;
        }

        int karmaAtual = karma.get(target);
        switch (operator) {
            case "++":
                cache.putIfAbsent(target + ":" + username, ++karmaAtual, 30, TimeUnit.SECONDS);
                break;

            case "--":
                cache.putIfAbsent(target + ":" + username, --karmaAtual, 30, TimeUnit.SECONDS);
                break;

            default:
                //do nothing
                break;

        }
        return String.format(KARMA_MESSAGE, target, cache.get(target + ":" + username));
    }

    /**
     * Recebe como parâmetro uma String que é todo o texto da mensagem recebida e verifica se há alguma string compatível
     * com o plugin.
     *
     * @param messageContent
     * @return true se a mensagem está no padrão correto para ser processada ou false caso contrário.
     * O padrão é String++ ou String--
     */
    private boolean canProcess(String messageContent) {
        boolean canProcess = null == messageContent ? false : FULL_MSG_PATTERN.matcher(messageContent).find();
        log.fine("Karma plugin - can process [" + messageContent + "] - " + canProcess);
        return canProcess;
    }
}