package br.com.jbugbrasil.commands.help;

import br.com.jbugbrasil.commands.Commands;
import br.com.jbugbrasil.utils.message.impl.MessageSender;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class HelpCommand extends BotCommand implements Commands {

    private Logger log = Logger.getLogger(HelpCommand.class.getName());
    private final ICommandRegistry commandRegistry;

    public HelpCommand(ICommandRegistry commandRegistry) {
        super(Commands.HELP, "Lista todos os comandos que este bot possui.");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder response = new StringBuilder("<b>Help</b> ");
        response.append("Comandos válidos:\n");

        for (BotCommand botCommand : commandRegistry.getRegisteredCommands()) {
            response.append(botCommand.toString()).append("\n");
        }

        SendMessage helpMessage = new SendMessage();
        helpMessage.setChatId(chat.getId().toString());
        helpMessage.enableHtml(true);
        helpMessage.disableWebPagePreview();
        helpMessage.setText(response.toString());

        MessageSender msg = new MessageSender(helpMessage);
        msg.send();
    }
}