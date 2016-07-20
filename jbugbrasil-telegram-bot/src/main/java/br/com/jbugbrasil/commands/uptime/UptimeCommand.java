package br.com.jbugbrasil.commands.uptime;

import br.com.jbugbrasil.commands.Commands;
import br.com.jbugbrasil.database.DatabaseOperations;
import br.com.jbugbrasil.database.impl.DatabaseProviderImpl;
import br.com.jbugbrasil.utils.Utils;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;

import java.text.ParseException;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class UptimeCommand extends BotCommand{

    private final Logger log = Logger.getLogger(UptimeCommand.class.getName());

    private DatabaseOperations db = new DatabaseProviderImpl();

    private final ICommandRegistry commandRegistry;

    public UptimeCommand(ICommandRegistry commandRegistry) {
        super(Commands.UPTIME, "Mostra o tempo que o Bot está no ar");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder response = new StringBuilder("*Uptime:* ");
        try {
            response.append("`" +Utils.upTime() + "` minuto(s).");

            SendMessage helpMessage = new SendMessage();
            helpMessage.setChatId(chat.getId().toString());
            helpMessage.enableMarkdown(true);
            helpMessage.setText(response.toString());
            absSender.sendMessage(helpMessage);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}