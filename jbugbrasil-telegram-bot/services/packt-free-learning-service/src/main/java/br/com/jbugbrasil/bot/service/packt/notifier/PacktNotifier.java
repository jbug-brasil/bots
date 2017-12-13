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

package br.com.jbugbrasil.bot.service.packt.notifier;

import br.com.jbugbrasil.bot.api.object.Chat;
import br.com.jbugbrasil.bot.api.object.Message;
import br.com.jbugbrasil.bot.api.object.MessageUpdate;
import br.com.jbugbrasil.bot.service.cache.qualifier.DefaultCache;
import br.com.jbugbrasil.bot.service.packt.pojo.PacktBook;
import br.com.jbugbrasil.bot.service.persistence.pojo.PacktNotification;
import br.com.jbugbrasil.bot.service.persistence.repository.PacktRepository;
import br.com.jbugbrasil.bot.telegram.api.message.sender.MessageSender;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.infinispan.Cache;

import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.logging.Logger;

@Stateless
@LocalBean
public class PacktNotifier {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private static final String FREE_LEARNING_URL = "https://www.packtpub.com/packt/offers/free-learning";
    private static final String PACKT_HOME_URL = "https://www.packtpub.com%s";

    @Inject
    @DefaultCache
    Cache<String, PacktBook> cache;

    @Inject
    private MessageSender messageSender;

    @Inject
    private PacktRepository repository;

    @Inject
    PacktBook packtBook;

    public String get() {
        StringBuilder builder = new StringBuilder("<i> Packt Free Learning - livro do dia</i>\n");
        builder.append("<b>Nome do Livro:</b> <code>" + packtBook.getBookName() + "</code>\n");
        builder.append("<b>Claim URL:</b> ");
        builder.append(packtBook.getClaimUrl());
        return builder.toString();
    }

    @Schedule(hour = "23", minute = "00")
    private void scheduler() {
        populate(false);
    }

    public void populate(boolean startup) {
        log.fine("Obtendo informações do livro grátis.");
        try {
            HttpResponse response = client().execute(new HttpGet(FREE_LEARNING_URL));
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String inputLine, previousLine = "";
            StringBuffer responseBuffer = new StringBuffer();

            while ((inputLine = reader.readLine()) != null) {
                // get the book name
                if (previousLine.contains("dotd-main-book-image")) {
                    inputLine = reader.readLine();
                    packtBook.setBookName(inputLine.split("/")[2].replace("\">", ""));
                }

                // get the claim URI
                if (inputLine.contains("/freelearning-claim/")) {
                    packtBook.setClaimUrl(String.format(PACKT_HOME_URL, inputLine.trim().replace("\"", "").replace(" id", "").split("=")[1]));
                }
                previousLine = inputLine;
            }
            log.fine(packtBook.toString());
            if (cache.containsKey("book")) {
                cache.replace("book", packtBook);
            } else {
                cache.put("book", packtBook);
            }

            if (startup) {
                return;
            } else {
                repository.get().stream().forEach(chatId -> this.notify(chatId));
            }
        } catch (final Exception e) {
            e.printStackTrace();
            log.warning("Falha ao obter informações do livro: " + e.getMessage());
        }
    }

    public String registerNotification(MessageUpdate message) {
        String channel = null;
        if (message.getMessage().getChat().getType().equals("group") || message.getMessage().getChat().getType().equals("supergroup")) {
            channel = message.getMessage().getChat().getTitle();
        } else {
            channel = message.getMessage().getFrom().getFirstName();
        }
        return repository.register(new PacktNotification(message.getMessage().getChat().getId(), channel));
    }

    public String unregisterNotification(MessageUpdate message) {
        String channel = null;
        if (message.getMessage().getChat().getType().equals("group") || message.getMessage().getChat().getType().equals("supergroup")) {
            channel = message.getMessage().getChat().getTitle();
        } else {
            channel = message.getMessage().getFrom().getFirstName();
        }
        return repository.unregister(new PacktNotification(message.getMessage().getChat().getId(), channel));
    }

    private void notify(BigInteger chatId) {
        Chat chat = new Chat();
        chat.setId(chatId.longValue());
        Message message = new Message();
        message.setChat(chat);
        message.setText(this.get());
        messageSender.processOutgoingMessage(message);
    }

    private CloseableHttpClient client() {
        return HttpClients.createDefault();
    }

}
