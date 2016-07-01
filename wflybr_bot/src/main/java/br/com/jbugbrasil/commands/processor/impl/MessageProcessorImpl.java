package br.com.jbugbrasil.commands.processor.impl;

import br.com.jbugbrasil.commands.Commands;
import br.com.jbugbrasil.commands.processor.MessageProcessor;
import br.com.jbugbrasil.commands.processor.PingProcessor;
import br.com.jbugbrasil.conf.BotConfig;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Created by fspolti on 7/1/16.
 */
public class MessageProcessorImpl implements MessageProcessor {


    @Override
    public SendMessage process(Update update) {

        SendMessage echoMessage = new SendMessage();

        echoMessage = userEnterOrLeftGroup(update);

        if (update.getMessage().getText() != null) {
            //Yeyyy karma fest
            if (update.getMessage().getText().contains("++")) {
                echoMessage.setChatId(update.getMessage().getChatId().toString());
                echoMessage.setText(String.format("%s tem %d pontos de karma",
                        update.getMessage().getText().substring(0, update.getMessage().getText().length() - 2), 1));
            } else if (update.getMessage().getText().contains("--")) {
                echoMessage.setChatId(update.getMessage().getChatId().toString());
                echoMessage.setText(String.format("%s tem %d pontos de karma",
                        update.getMessage().getText().substring(0, update.getMessage().getText().length() - 2), 1));
            }
        }

        if (update.getMessage().getText() != null && update.getMessage().getText().toLowerCase().startsWith(Commands.PING)) {
            PingProcessor ping = new PingProcessor();
            echoMessage = ping.process(update);
        }

        return echoMessage;

    }

    private SendMessage userEnterOrLeftGroup (Update update) {
        SendMessage echoMessage = new SendMessage();
        //Welcome dude
        if (update.getMessage().getNewChatMember() != null) {
            echoMessage.setChatId(update.getMessage().getChatId().toString());
            echoMessage.setText(String.format(BotConfig.WELCOME_MESSAGE, update.getMessage().getNewChatMember().getFirstName()));

        } else //Oh boy, someone left us
            if (update.getMessage().getLeftChatMember() != null) {
                echoMessage.setChatId(update.getMessage().getChatId().toString());
                echoMessage.setText(String.format(BotConfig.GOODBYE_MESSAGE, update.getMessage().getLeftChatMember().getFirstName()));
            }
        return echoMessage;
    }

    @Override
    public SendMessage reply(SendMessage message) {
        return message;
    }

    @Override
    public boolean canProcess(String messageContent) {
        return false;
    }

}