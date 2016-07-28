package br.com.jbugbrasil.commands.processor;

import br.com.jbugbrasil.commands.Commands;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
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
    public boolean canProcess(String messageContent) {
        return false;
    }
}