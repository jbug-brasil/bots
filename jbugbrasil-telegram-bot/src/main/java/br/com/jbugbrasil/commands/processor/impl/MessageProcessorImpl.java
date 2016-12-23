package br.com.jbugbrasil.commands.processor.impl;

import br.com.jbugbrasil.commands.Commands;
import br.com.jbugbrasil.commands.processor.KarmaProcessor;
import br.com.jbugbrasil.commands.processor.MessageProcessor;
import br.com.jbugbrasil.commands.processor.PingProcessor;
import br.com.jbugbrasil.conf.BotConfig;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class MessageProcessorImpl implements MessageProcessor {

    private MessageProcessor ping = new PingProcessor();
    private MessageProcessor karma = new KarmaProcessor();

    @Override
    public SendMessage process(Update update) {
        SendMessage echoMessage = new SendMessage();

        if (update.getEditedMessage() == null) {

            // is a new user? Or maybe someone left the group?
            echoMessage = userEnterOrLeftGroup(update);

            if (update.getMessage().getText() != null) {
                //Yeyyy karma fest
                echoMessage = karma.process(update);
            }

            if (update.getMessage().getText() != null && update.getMessage().getText().toLowerCase().equals(Commands.PING)) {
                echoMessage = ping.process(update);
            }
            return echoMessage;
        }
        return echoMessage;
    }

    private SendMessage userEnterOrLeftGroup(Update update) {

        SendMessage echoMessage = new SendMessage();
        //Welcome dude
        if (null != update.getMessage().getNewChatMember()) {
            if  (!update.getMessage().getNewChatMember().getUserName().equals(BotConfig.JBUG_BRASIL_BOT_USER)) {
                echoMessage.setChatId(update.getMessage().getChatId().toString());
                echoMessage.setText(String.format(BotConfig.WELCOME_MESSAGE, update.getMessage().getNewChatMember().getFirstName(), "JBug Brasil"));
            }
        } else //Oh boy, someone left us
            if (null != update.getMessage().getLeftChatMember()) {
                if  (!update.getMessage().getLeftChatMember().getUserName().equals(BotConfig.JBUG_BRASIL_BOT_USER)) {
                    echoMessage.setChatId(update.getMessage().getChatId().toString());
                    echoMessage.setText(String.format(BotConfig.GOODBYE_MESSAGE, update.getMessage().getLeftChatMember().getFirstName()));
                }
            }
        return echoMessage;
    }

    @Override
    public boolean canProcess(String messageContent) {
        return false;
    }

}