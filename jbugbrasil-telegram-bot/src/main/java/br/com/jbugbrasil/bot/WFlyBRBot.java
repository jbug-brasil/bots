package br.com.jbugbrasil.bot;

import br.com.jbugbrasil.commands.getbooks.GetBooksCommand;
import br.com.jbugbrasil.commands.getkarma.GetKarma;
import br.com.jbugbrasil.commands.help.HelpCommand;
import br.com.jbugbrasil.commands.processor.impl.MessageProcessorImpl;
import br.com.jbugbrasil.conf.BotConfig;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 *         part of this code was obtained from https://github.com/rubenlagus/TelegramBotsExample
 */
public class WFlyBRBot extends TelegramLongPollingCommandBot {

    private Logger log = Logger.getLogger(WFlyBRBot.class.getName());

    HelpCommand helpCommand = new HelpCommand(this);
    GetBooksCommand getbooks = new GetBooksCommand(this);
    GetKarma getkarma = new GetKarma(this);

    MessageProcessorImpl p = new MessageProcessorImpl();

    /**
     * Constructor.
     */
    public WFlyBRBot() {

        // register the commands here
        register(helpCommand);
        register(getbooks);
        register(getkarma);

        // This is not necessary at this moment, we can add other bots that have other commands, lets remove this to
        // avoid spam.
//        registerDefaultAction((absSender, message) -> {
//            SendMessage commandUnknownMessage = new SendMessage();
//            commandUnknownMessage.setChatId(message.getChatId().toString());
//            commandUnknownMessage.setText("O comando '" + message.getText() + "' não é válido, veja a lista de comandos abaixo:");
//            try {
//                absSender.sendMessage(commandUnknownMessage);
//            } catch (TelegramApiException e) {
//                log.severe("Ocorreu algum erro ao tentar enviar a mensagem: " + e.getCause());
//            }
//            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
//        });
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
        return BotConfig.WFLYBR_USER;
    }

    @Override
    public String getBotToken() {
        return BotConfig.WFLYBR_TOKEN;
    }

}