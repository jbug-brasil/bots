package br.com.jbugbrasil.utils.message.impl;

import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.utils.message.Message;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class MessageSender extends TelegramLongPollingBot implements Message {

    private final Logger log = Logger.getLogger(MessageSender.class.getName());

    private SendMessage message;

    public MessageSender(SendMessage message) {
        this.message = message;
    }

    @Override
    public void send() {
        message.disableWebPagePreview();
        try {
            sendMessage(message);
        } catch (TelegramApiException e) {
            log.severe("Falha ao enviar mensagem: " + e.getCause());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return BotConfig.JBUG_BRASIL_BOT_USER;
    }

    @Override
    public String getBotToken() {
        return BotConfig.JBUG_BRASIL_BOT_TOKEN;
    }
}