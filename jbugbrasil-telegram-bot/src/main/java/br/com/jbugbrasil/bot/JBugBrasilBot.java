package br.com.jbugbrasil.bot;

import br.com.jbugbrasil.commands.faq.FaqCommand;
import br.com.jbugbrasil.commands.getbooks.GetBooksCommand;
import br.com.jbugbrasil.commands.getkarma.GetKarma;
import br.com.jbugbrasil.commands.help.HelpCommand;
import br.com.jbugbrasil.commands.processor.impl.MessageProcessorImpl;
import br.com.jbugbrasil.commands.uptime.UptimeCommand;
import br.com.jbugbrasil.commands.urbandictionary.UrbanDictionaryCommand;
import br.com.jbugbrasil.conf.BotConfig;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.bots.commands.CommandRegistry;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 *         part of this code was obtained from https://github.com/rubenlagus/TelegramBotsExample
 */
public class JBugBrasilBot extends TelegramLongPollingCommandBot {

    private final CommandRegistry commandRegistry = new CommandRegistry(true, null);
    //Commands
    private final HelpCommand helpCommand = new HelpCommand(this);
    private final GetBooksCommand getbooks = new GetBooksCommand(this);
    private final GetKarma getkarma = new GetKarma(this);
    private final UptimeCommand uptime = new UptimeCommand(this);
    private final FaqCommand faq = new FaqCommand(this);
    private final UrbanDictionaryCommand ub = new UrbanDictionaryCommand(this);
    //Message Processor
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
        register(uptime);
        register(faq);
        register(ub);

        // :-(  do this to be able to answer commands like /command@botName
        registerDefaultAction((absSender, message) -> {

            //corrigir isso, se algum comando for sem parametro, pode dar erro, verificar uma forma melhor de obter os parametros
            String[] params = message.getText().substring(message.getText().indexOf("@" + BotConfig.JBUG_BRASIL_BOT_USER) + BotConfig.JBUG_BRASIL_BOT_USER.length() + 1, message.getText().length()).split(" ");
            if (message.getText().contains("@" + BotConfig.JBUG_BRASIL_BOT_USER)) {
                switch (message.getText().substring(1, message.getText().indexOf("@"))) {
                    case "help":
                        helpCommand.execute(absSender, message.getFrom(), message.getChat(), params);
                        break;

                    case "books":
                        getbooks.execute(absSender, message.getFrom(), message.getChat(), params);
                        break;

                    case "karma":
                        try {
                            params[0] = message.getText().substring(message.getText().indexOf("@" + BotConfig.JBUG_BRASIL_BOT_USER) + BotConfig.JBUG_BRASIL_BOT_USER.length() + 1, message.getText().length()).trim();
                            getkarma.execute(absSender, message.getFrom(), message.getChat(), params);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "uptime":
                        uptime.execute(absSender, message.getFrom(), message.getChat(), params);
                        break;

                    case "faq":
                        faq.execute(absSender, message.getFrom(), message.getChat(), params);
                        break;

                    case "ub":
                        ub.execute(absSender,message.getFrom(), message.getChat(), params);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        log.fine(String.valueOf(update));
        // process the message and send a response, if anything is there.
        try {
            sendMessage(p.process(update));
        } catch (TelegramApiException e) {
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