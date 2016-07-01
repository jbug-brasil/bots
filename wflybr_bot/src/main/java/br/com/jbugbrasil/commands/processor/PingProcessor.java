package br.com.jbugbrasil.commands.processor;

import br.com.jbugbrasil.commands.Commands;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Created by fspolti on 7/1/16.
 */
public class PingProcessor implements MessageProcessor {

    @Override
    public SendMessage process(Update update) {

        SendMessage echoMessage = new SendMessage();
        echoMessage.setChatId(update.getMessage().getChatId().toString());

        if (update.getMessage().getFrom().getUserName() != null) {
            echoMessage.setText(String.format(Commands.PONG, "@" + update.getMessage().getFrom().getUserName()));
        } else {
            echoMessage.setReplyToMessageId(update.getMessage().getMessageId());
            echoMessage.setText(String.format(Commands.PONG, update.getMessage().getFrom().getFirstName()));
        }

        return echoMessage;
    }

    @Override
    public SendMessage reply(SendMessage message) {


        return null;
    }

    @Override
    public boolean canProcess(String messageContent) {
        return false;
    }
}

