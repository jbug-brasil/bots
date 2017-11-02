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

package br.com.jbugbrasil.bot.telegram.api;

import br.com.jbugbrasil.bot.api.conf.systemproperties.BotProperty;
import br.com.jbugbrasil.bot.api.object.GetUpdatesConfProducer;
import br.com.jbugbrasil.bot.api.object.Message;
import br.com.jbugbrasil.bot.api.object.MessageUpdate;
import br.com.jbugbrasil.bot.api.object.TelegramResponse;
import br.com.jbugbrasil.bot.telegram.api.httpclient.BotCloseableHttpClient;

import br.com.jbugbrasil.bot.telegram.api.polling.JBugBrasilLongPoolingBot;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Logger;

@ApplicationScoped
public class UpdatesReceiver implements Runnable {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final String TELEGRAM_UPDATE_ENDPOINT = "https://api.telegram.org/bot%s/getUpdates";

    @Inject
    @BotProperty(name = "br.com.jbugbrasil.bot.telegram.token", required = true)
    String botTokenId;

    @Inject
    @BotProperty(name = "br.com.jbugbrasil.bot.telegram.userId", required = true)
    String botUserId;

    @Inject
    private BotCloseableHttpClient httpClient;

    private Long lastUpdateId = 0L;

    private volatile boolean running = false;

    private RequestConfig requestConfig;

    private Thread currentThread;

    @Inject
    JBugBrasilLongPoolingBot callback;

    /**
     * Método responsável por configurar o HttpClient e iniciar o receiver chamando o método <b>run</b> desta classe.
     */
    public synchronized void start() {
        requestConfig = RequestConfig.copy(RequestConfig.custom().build())
                .setSocketTimeout(75 * 1000)
                .setConnectTimeout(75 * 1000)
                .setConnectionRequestTimeout(75 * 1000).build();
        running = true;
        currentThread = new Thread(this);
        currentThread.setDaemon(true);
        currentThread.setName("Telegram-" + botUserId);
        currentThread.start();
    }

    /**
     * Quando chamado, interrompe esta thread.
     */
    public void interrupt() {
        if (running) {
            running = false;
            currentThread.interrupt();
        }
    }

    /**
     * Inicia o Receiver, cada nova mensagem enviada a será recebida e irá notificar os bots que implementam esta api através da
     * chamada <b>callback.onUpdateReceived(u);</b>
     * Sua configuração é feita atraǘes da classe {@link GetUpdatesConfProducer}
     *
     * Esta thread permanece em execução juntamente com o bot que o implementa até o fim de seu ciclo de vida.
     * O intervalo de cada request é <b>600ms</b>
     *
     */
    @Override
    public synchronized void run() {
        ObjectMapper objectMapper = new ObjectMapper();
        while (running) {
            GetUpdatesConfProducer getUpdates = new GetUpdatesConfProducer().setLimit(100).setTimeout(10 * 1000).setOffset(lastUpdateId + 1);
            log.finest("receiver config -> " + getUpdates.toString());
            try {
                String url = String.format(TELEGRAM_UPDATE_ENDPOINT, botTokenId);

                HttpPost httpPost = new HttpPost(url);
                httpPost.addHeader("charset", StandardCharsets.UTF_8.name());
                httpPost.setConfig(requestConfig);
                httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(getUpdates), ContentType.APPLICATION_JSON));

                try (CloseableHttpResponse response = httpClient.get().execute(httpPost)) {
                    HttpEntity responseEntity = response.getEntity();
                    BufferedHttpEntity buf = new BufferedHttpEntity(responseEntity);
                    String responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);

                    if (response.getStatusLine().getStatusCode() != 200) {
                        log.warning("Erro ao receber resposta da API do Telegram, código de status é " + response.getStatusLine().getStatusCode());
                        synchronized (this) {
                            this.wait(600);
                        }
                    }

                    TelegramResponse<ArrayList<MessageUpdate>> updates = objectMapper.readValue(responseContent,
                            new TypeReference<TelegramResponse<ArrayList<MessageUpdate>>>() {
                            });

                    if (updates.getResult().isEmpty()) {
                        this.wait(600);
                    }

                    updates.getResult().removeIf(n -> n.getUpdateId() < lastUpdateId);
                    lastUpdateId = updates.getResult().parallelStream().map(MessageUpdate::getUpdateId).max(Long::compareTo).orElse(0L);
                    updates.getResult().stream().forEach(u -> {
                        // make sure that even edited messages will be intercepted by the bot.
                        if (null != u.getEditedMessage()) {
                            log.finest("is updated message? " + true);
                            Message msg = new Message();
                            msg.setChat(u.getEditedMessage().getChat());
                            msg.setDate(u.getEditedMessage().getDate());
                            msg.setEntities(u.getEditedMessage().getEntities());
                            msg.setFrom(u.getEditedMessage().getFrom());
                            msg.setMessageId(u.getEditedMessage().getMessageId());
                            msg.setText(u.getEditedMessage().getText());
                            u.setMessage(msg);
                        }
                        // notify the implementations of JBugBrasilLongPoolingBot about the received messages.
                        log.finest("Message is [ " + u.toString() + "]");
                        callback.onUpdateReceived(u);

                    });
                    // wait 600ms before check for updates
                    this.wait(600);

                }
            } catch (final Exception e) {
                e.printStackTrace();
                log.warning("Erro encontrado " + e.getMessage());
            }
        }
    }

}