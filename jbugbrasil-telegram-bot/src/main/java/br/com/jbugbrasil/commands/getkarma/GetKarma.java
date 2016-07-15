package br.com.jbugbrasil.commands.getkarma;

import br.com.jbugbrasil.commands.Commands;
import br.com.jbugbrasil.commands.processor.MessageProcessor;
import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.database.DatabaseOperations;
import br.com.jbugbrasil.database.impl.DatabaseProviderImpl;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class GetKarma extends BotCommand implements MessageProcessor {

    private final Logger log = Logger.getLogger(GetKarma.class.getName());

    private final ICommandRegistry commandRegistry;
    private final DatabaseOperations db = new DatabaseProviderImpl();

    public GetKarma(ICommandRegistry commandRegistry) {
        super(Commands.GET_KARMA, "Pesquisa o karma do usuário desejado, Ex: /getkarma spolti");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder response = new StringBuilder("<b>Pesquisar Karma</b>\n ");
        SendMessage responseMessage = new SendMessage();

        responseMessage.setChatId(chat.getId().toString());
        responseMessage.enableHtml(true);

        try {
            log.info("Pesquisando o karma do usuário " + strings[0]);

            //getting the value from database.
            response.append(String.format(BotConfig.GET_KARMA_RESPONSE, strings[0].trim(), db.getKarmaPoints(strings[0])));
            responseMessage.setText(response.toString());


        } catch (ArrayIndexOutOfBoundsException e) {
            response.append("Username é obrigatório.");
            responseMessage.setText(response.toString());
        }

        try {
            absSender.sendMessage(responseMessage);
        } catch (TelegramApiException e) {
            log.severe("Ocorreu um erro ao processar o comando /getkarma: " + e.getCause());
        }
    }

    @Override
    public SendMessage process(Update update) {
        return null;
    }

    @Override
    public SendMessage reply(SendMessage message) {
        return null;
    }

    @Override
    public boolean canProcess(String messageContent) {
        return false ? messageContent.isEmpty() : true;
    }
}