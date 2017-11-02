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

package br.com.jbugbrasil.bot.telegram.api.message.sender;

import br.com.jbugbrasil.bot.api.conf.systemproperties.BotProperty;
import br.com.jbugbrasil.bot.api.object.Message;
import br.com.jbugbrasil.bot.telegram.api.httpclient.BotCloseableHttpClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class MessageSender implements Sender {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final String TELEGRAM_API_SENDER_ENDPOINT = "https://api.telegram.org/bot%s/sendMessage";

    @Inject
    @BotProperty(name = "br.com.jbugbrasil.bot.telegram.token", required = true)
    String botTokenId;

    @Inject
    private BotCloseableHttpClient httpClient;

    @Override
    public void processOutgoingMessage(Message message) {
        try {
            if (message.getText().length() > 1) {
                log.fine("Enviando mensagem: [" + message.getText() + "]");
                send(message);
            }
        } catch (final Exception e) {
            log.finest("Erro ao enviar msg " + e.getMessage());
        }
    }

    /**
     * Prepara a request e envia a mensagem para a API do telegram para o grupo ou chat destinatário da mensagem.
     * Parâmetros necessários para efetuar a request:
     *  - chat_id = chat ou grupo
     *  - parse_mode = default HTML, markdown há muitos problemas de formatação nos diversos clients disponíveis.
     *  - reply_to_message_id = Id de uma mensagem enviada, se presente a mensagem será respondida ao seu remetente original
     *  - disable_web_page_preview = desabilita pré vizualização de links.
     * @param message Mensagem a ser enviada
     */
    private void send(Message message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String url = String.format(TELEGRAM_API_SENDER_ENDPOINT, botTokenId);
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("charset", StandardCharsets.UTF_8.name());
            httpPost.addHeader("content-type", "application/json");
            Map<String, String> body = new HashMap<>();
            body.put("chat_id", message.getChat().getId() + "");
            body.put("parse_mode", "HTML");
            body.put("reply_to_message_id", message.getMessageId() + "");
            body.put("text", message.getText());
            body.put("disable_web_page_preview", "true");
            httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(body), ContentType.APPLICATION_JSON));
            try (CloseableHttpResponse response = httpClient.get().execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                BufferedHttpEntity buf = new BufferedHttpEntity(responseEntity);
                String responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);
                log.fine("Telegram API response: [" + responseContent + "]");
            } catch (final Exception e) {
                e.printStackTrace();
                log.warning("Erro encontrado " + e.getMessage());
            }
        } catch (final Exception e) {
            e.printStackTrace();
            log.warning("Erro encontrado " + e.getMessage());
        }
    }
}