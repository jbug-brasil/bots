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

package br.com.jbugbrasil.bot.plugin.welcome;

import br.com.jbugbrasil.bot.api.conf.systemproperties.BotProperty;
import br.com.jbugbrasil.bot.api.emojis.Emoji;
import br.com.jbugbrasil.bot.api.object.LeftChatMember;
import br.com.jbugbrasil.bot.api.object.Message;
import br.com.jbugbrasil.bot.api.object.MessageUpdate;
import br.com.jbugbrasil.bot.api.object.NewChatMember;
import br.com.jbugbrasil.bot.api.spi.PluginProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class WelcomeMessagePlugin implements PluginProvider {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final String WELCOME_MESSAGE = "Olá <b>%s</b>, seja bem vindo(a) ao grupo %s. Aqui você poderá discutir sobre" +
            " os projetos opensource da família JBoss e também outros projetos open source. Para conhecer o bot, /help. " + Emoji.SMILING_FACE_WITH_OPEN_MOUTH;
    private final String GOODBYE_MESSAGE = "Tínhamos um traidor entre nós, <b>%s</b> nos deixou. " + Emoji.ANGRY_FACE;

    @Override
    public String process(MessageUpdate update) {
        return chatMember(update);
    }

    @Override
    public void load() {
        log.fine("Plugin welcome-message-plugin ativado.");
    }


    /**
     * Quando um membro, entrar, sair ou for excluído uma memsagem será disparada no grupo.
     *
     * @param update {@link MessageUpdate}
     * @return true if the message is to inform a new member or if a member left the chat
     */
    private String chatMember(MessageUpdate update) {
        ObjectMapper mapper = new ObjectMapper();
        final Message message = new Message();
        for (Map.Entry<String, Object> entry : update.getMessage().getAdditionalProperties().entrySet()) {
            log.fine("Additional Properties: KEY + " + entry.getKey() + " - VALUE " + entry.getValue().toString());
            if (entry.getKey().equals("new_chat_member")) {
                NewChatMember member = mapper.convertValue(entry.getValue(), NewChatMember.class);
                message.setText(String.format(WELCOME_MESSAGE, member.getFirst_name(), update.getMessage().getChat().getTitle()));

            } else if (entry.getKey().equals("left_chat_participant")) {
                LeftChatMember member = mapper.convertValue(entry.getValue(), LeftChatMember.class);
                message.setText(String.format(GOODBYE_MESSAGE, member.getFirst_name()));
            }
        }
        return message.getText();
    }
}