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

package br.com.jbugbrasil.bot.telegram.api.message;

import br.com.jbugbrasil.bot.api.conf.systemproperties.BotProperty;
import br.com.jbugbrasil.bot.api.object.Message;
import br.com.jbugbrasil.bot.api.object.MessageUpdate;
import br.com.jbugbrasil.bot.api.spi.CommandProvider;
import br.com.jbugbrasil.bot.api.spi.PluginProvider;
import br.com.jbugbrasil.bot.telegram.api.message.sender.MessageSender;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class OutcomeMessageProcessor implements Processor {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    @BotProperty(name = "br.com.jbugbrasil.bot.telegram.token", required = true)
    String botTokenId;
    @Inject
    @BotProperty(name = "br.com.jbugbrasil.bot.telegram.userId", required = true)
    String botUserId;
    @Inject
    private Instance<CommandProvider> command;
    @Inject
    private Instance<PluginProvider> plugin;
    @Inject
    private MessageSender reply;

    @Override
    public void reply(MessageUpdate messageUpdate) {
        nonCommandProcessor(messageUpdate);
        if (null != messageUpdate.getMessage().getText()) {
            if (messageUpdate.getMessage().getText().startsWith("/")) {
                if (messageUpdate.getMessage().getText().contains("@" + botUserId) || !messageUpdate.getMessage().getText().contains("@")) {
                    commandProcessor(messageUpdate);
                } else {
                    log.fine("Comando digitado não foi direcionado a este bot: [" +  messageUpdate.getMessage().getText() + "]");
                }
            }
        }
    }

    @Override
    public void commandProcessor(MessageUpdate messageUpdate) {
        final StringBuilder response = new StringBuilder("");
        final Message message = new Message();
        log.fine("Processing command: " + messageUpdate.getMessage().getText());

        String[] args = messageUpdate.getMessage().getText().split(" ");
        // neste ponto, o bot já aceitou o comando /comando@btId,
        String command2process = args[0].replace("@" + botUserId, "");

        if (command2process.equals("/help")) {
            command.forEach(c -> {
                response.append(c.help() + "\n");
            });
        }
        command.forEach(command -> {

            if (command.name().equals(command2process)) {
                if (concat(args).equals("help")) {
                    response.append(command.help());
                } else {
                    response.append(command.execute(Optional.of(concat(args)), messageUpdate));
                    log.fine("COMMAND_PROCESSOR - Comando processado, resultado é: " + response);
                }
            }
        });
        if (response.length() < 1) {
            log.warning("COMMAND_PROCESSOR - Comando " + command2process + " não encontrado.");
        }
        message.setChat(messageUpdate.getMessage().getChat());
        message.setText(response.toString());
        message.setMessageId(messageUpdate.getMessage().getMessageId());
        reply.processOutgoingMessage(message);
    }

    @Override
    public void nonCommandProcessor(MessageUpdate messageUpdate) {
        log.fine("NON_COMMAND_PROCESSOR - Processando mensagem: " + messageUpdate.getMessage().toString());
        Message message = new Message();
        message.setChat(messageUpdate.getMessage().getChat());
        message.setMessageId(messageUpdate.getMessage().getMessageId());

        plugin.forEach(plugin -> {
            message.setText(plugin.process(messageUpdate));
            try {
                if (null != message.getText()) {
                    // karma não precisa ser respondido para a mensagem
                    if (message.getText().contains("karma")) message.setMessageId(0);
                    reply.processOutgoingMessage(message);
                }
            } catch (final Exception e) {
                log.fine("NON_COMMAND_PROCESSOR - Mensagem não processada pelos plugins disponíveis");
            }
        });
    }

    /**
     * Parse the parameters received into a single String and make it lower case
     *
     * @param parameters
     * @return the formatted string
     */
    private String concat(String... parameters) {
        String result = "";
        for (int i = 1; i < parameters.length; i++) {
            if (parameters.length > i + 1) {
                result += parameters[i] + " ";
            } else {
                result += parameters[i];
            }
        }
        return result.toLowerCase();
    }


}