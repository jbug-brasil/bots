package br.com.jbugbrasil.commands.getbooks;

import br.com.jbugbrasil.commands.Commands;
import br.com.jbugbrasil.gitbooks.GitBooks;
import br.com.jbugbrasil.gitbooks.impl.GitBooksImpl;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class GetBooksCommand extends BotCommand implements Commands {

    private final Logger log = Logger.getLogger(GetBooksCommand.class.getName());

    private final ICommandRegistry commandRegistry;
    private final GitBooks gitbooks = new GitBooksImpl();

    public GetBooksCommand(ICommandRegistry commandRegistry) {
        super(Commands.GET_BOOKS, "Lista todos os livros disponíveis em https://www.gitbook.com/@jboss-books");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder response = new StringBuilder("<b>Livros Disponíveis</b>: ");
        response.append("\n" + gitbooks.getBooks());

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
}