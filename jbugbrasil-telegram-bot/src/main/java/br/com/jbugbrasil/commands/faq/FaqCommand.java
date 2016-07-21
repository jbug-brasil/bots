package br.com.jbugbrasil.commands.faq;

import br.com.jbugbrasil.commands.Commands;
import br.com.jbugbrasil.commands.processor.MessageProcessor;
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
public class FaqCommand extends BotCommand implements Commands, MessageProcessor {

    private final Logger log = Logger.getLogger(FaqCommand.class.getName());

    private final ICommandRegistry commandRegistry;

    public FaqCommand(ICommandRegistry commandRegistry) {
        super(Commands.FAQ, "Lista todos os projetos JBoss.");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder response = new StringBuilder("<b>Lista dos Projetos</b>: ");
        response.append("Em desenvolvimento");
        SendMessage helpMessage = new SendMessage();
        helpMessage.setChatId(chat.getId().toString());
        helpMessage.enableHtml(true);
        helpMessage.setText(response.toString());
        try {
            absSender.sendMessage(helpMessage);
        } catch (TelegramApiException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public SendMessage process(Update update) {
        return null;
    }

    @Override
    public boolean canProcess(String messageContent) {
        return false;
    }
}