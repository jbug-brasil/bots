package br.com.jbugbrasil.bot;

import br.com.jbugbrasil.commands.Commands;
import br.com.jbugbrasil.commands.getbooks.GetBooksCommand;
import br.com.jbugbrasil.commands.help.HelpCommand;
import br.com.jbugbrasil.conf.BotConfig;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 * part of this code was obtained from https://github.com/rubenlagus/TelegramBotsExample
 */
public class WFlyBRBot extends TelegramLongPollingCommandBot {

    public static final String LOGTAG = "COMMANDSHANDLER";
    HelpCommand helpCommand = new HelpCommand(this);
    GetBooksCommand getbooks = new GetBooksCommand(this);

    /**
     * Constructor.
     */
    public WFlyBRBot() {

        register(helpCommand);
        register(getbooks);

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId().toString());
            commandUnknownMessage.setText("O comando '" + message.getText() + "' não é válido, veja a lista de comandos abaixo:");
            try {
                absSender.sendMessage(commandUnknownMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        SendMessage echoMessage = new SendMessage();
        Message message;

        System.out.println(update);

        //Welcome dude
        if (update.getMessage().getNewChatMember() != null) {
            echoMessage.setChatId(update.getMessage().getChatId().toString());
            echoMessage.setText(String.format(BotConfig.WELCOME_MESSAGE, update.getMessage().getNewChatMember().getFirstName()));
        }

        //Oh boy, someone left us
        if (update.getMessage().getLeftChatMember() != null) {
            echoMessage.setChatId(update.getMessage().getChatId().toString());
            echoMessage.setText(String.format(BotConfig.GOODBYE_MESSAGE, update.getMessage().getLeftChatMember().getFirstName()));
        }

        if (update.getMessage().getText() != null) {
            //Yeyyy karma fest
            if (update.getMessage().getText().contains("++")) {
                echoMessage.setChatId(update.getMessage().getChatId().toString());
                echoMessage.setText(String.format("%s tem %d pontos de karma",
                        update.getMessage().getText().substring(0, update.getMessage().getText().length() -2 ), 1));
            } else if (update.getMessage().getText().contains("--")) {
                echoMessage.setChatId(update.getMessage().getChatId().toString());
                echoMessage.setText(String.format("%s tem %d pontos de karma",
                        update.getMessage().getText().substring(0, update.getMessage().getText().length() -2 ), 1));
            }

            //Hey, do you want to ping me bro?
            if (update.getMessage().getText().startsWith(Commands.PING)) {
                echoMessage.setChatId(update.getMessage().getChatId().toString());
                echoMessage.setReplyToMessageId(update.getMessage().getMessageId());
                echoMessage.setText(String.format(Commands.PONG, update.getMessage().getFrom().getUserName()));
            }
        }


        // try to send a response if there is some content on echoMessage
        try {
                sendMessage(echoMessage);
        } catch (TelegramApiException e) {
//            BotLogger.error(LOGTAG, e);
            //do nothing
        }
    }


    @Override
    public String getBotUsername() {
        return BotConfig.WFLYBR_USER;
    }

    @Override
    public String getBotToken() {
        return BotConfig.WFLYBR_TOKEN;
    }

}