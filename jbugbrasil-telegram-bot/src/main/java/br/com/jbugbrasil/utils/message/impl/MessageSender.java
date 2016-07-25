package br.com.jbugbrasil.utils.message.impl;

import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.utils.message.Message;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class MessageSender extends TelegramLongPollingBot implements Message{

    private SendMessage message;

    public MessageSender (SendMessage message) {
        this.message = message;
    }

    @Override
    public void send() {

        //disable the web site preview (IMHO it is spammy)
        message.disableWebPagePreview();

        try {
            sendMessage(message);
        } catch (TelegramApiException e) {
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