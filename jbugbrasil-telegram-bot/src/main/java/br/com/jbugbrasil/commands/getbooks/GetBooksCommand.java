package br.com.jbugbrasil.commands.getbooks;

import br.com.jbugbrasil.commands.Commands;
import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.gitbooks.GitBooks;
import br.com.jbugbrasil.gitbooks.impl.GitBooksImpl;
import br.com.jbugbrasil.utils.message.impl.MessageSender;
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

    private final ICommandRegistry commandRegistry;
    private final GitBooks gitbooks = new GitBooksImpl();
    private final SendMessage getBooksCommandResponse = new SendMessage();

    public GetBooksCommand(ICommandRegistry commandRegistry) {
        super(Commands.GET_BOOKS, "Lista os livros disponíveis em https://www.gitbook.com/@jboss-books");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder response = new StringBuilder("*Livros Disponíveis*: ");
        response.append("\n[" + gitbooks.getBooks() + "](" + BotConfig.GIT_BOOKS_URL + ")");

        getBooksCommandResponse.setChatId(chat.getId().toString());
        getBooksCommandResponse.enableMarkdown(true);
        getBooksCommandResponse.setText(response.toString());

        MessageSender message = new MessageSender(getBooksCommandResponse);
        message.send();

    }
}