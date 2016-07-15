package br.com.jbugbrasil.bot;

import br.com.jbugbrasil.commands.getbooks.GetBooksCommand;
import br.com.jbugbrasil.commands.getkarma.GetKarma;
import br.com.jbugbrasil.commands.help.HelpCommand;
import br.com.jbugbrasil.commands.processor.impl.MessageProcessorImpl;
import br.com.jbugbrasil.conf.BotConfig;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.bots.commands.CommandRegistry;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 *         part of this code was obtained from https://github.com/rubenlagus/TelegramBotsExample
 */
public class JBugBrasilBot extends TelegramLongPollingCommandBot {

    private final CommandRegistry commandRegistry = new CommandRegistry();
    HelpCommand helpCommand = new HelpCommand(this);
    GetBooksCommand getbooks = new GetBooksCommand(this);
    GetKarma getkarma = new GetKarma(this);
    MessageProcessorImpl p = new MessageProcessorImpl();
    private Logger log = Logger.getLogger(JBugBrasilBot.class.getName());

    /**
     * Constructor.
     */
    public JBugBrasilBot() {

        // register the commands here
        register(helpCommand);
        register(getbooks);
        register(getkarma);

        // :-(
        registerDefaultAction((absSender, message) -> {
            String[] param = new String[1];
            if (message.getText().contains("@" + BotConfig.JBUG_BRASIL_BOT_USER)) {
                switch (message.getText().substring(1, message.getText().indexOf("@"))) {
                    case "help":
                        helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
                        break;

                    case "getbooks":
                        getbooks.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
                        break;

                    case "getkarma":
                        try {

                            param[0] = message.getText().substring(message.getText().indexOf("@" + BotConfig.JBUG_BRASIL_BOT_USER) + BotConfig.JBUG_BRASIL_BOT_USER.length() + 1, message.getText().length()).trim();
                            getkarma.execute(absSender, message.getFrom(), message.getChat(), param);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //do nothing
                            log.info("aconteceu algo errado auheuaehauhe ");
                            e.printStackTrace();
                        }
                        break;

                    default:
                        //do nothing
                        break;
                }
            }
        });
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        log.info(String.valueOf(update));

        // process the message and send a response, if is there anything.
        try {
            sendMessage(p.process(update));
        } catch (TelegramApiException e) {
            //do nothing
        }
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