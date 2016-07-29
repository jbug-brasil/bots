package br.com.jbugbrasil.commands.faq;

import br.com.jbugbrasil.cache.CacheProviderImpl;
import br.com.jbugbrasil.commands.Commands;
import br.com.jbugbrasil.commands.processor.MessageProcessor;
import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.utils.message.impl.MessageSender;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;

import java.util.List;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class FaqCommand extends BotCommand implements Commands, MessageProcessor {

    private static final CacheProviderImpl cache = CacheProviderImpl.getInstance();
    private final ICommandRegistry commandRegistry;

    public FaqCommand(ICommandRegistry commandRegistry) {
        super(Commands.FAQ, "Pesquisa informações sobre os projetos cadastrados no bot. Ex: /faq hibernate");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        SendMessage faqMessage = new SendMessage();
        faqMessage.setChatId(chat.getId().toString());
        faqMessage.enableMarkdown(true);
        faqMessage.setText(query(parseParameters(strings)));
        MessageSender msg = new MessageSender(faqMessage);
        msg.send();
    }

    /*
    * Perform a very embracing query in the cache
    * @param String key
    * @returns a list containing the result
    */
    private String query(String key) {

        StringBuilder stbuilder = new StringBuilder();

        // get the query factory for the cache
        QueryFactory<?> qf = Search.getQueryFactory(cache.getCache());

        // Build the query
        Query q = qf.from(Project.class).having("id").like("%" + key + "%").toBuilder().build();
        // Perform the query
        List<Project> result = q.list();

        if (q.getResultSize() == 0) {
            return String.format(BotConfig.PROJECT_NOT_FOUND_MESSAGE, key);
        }

        for (Project project : result) {
            stbuilder.append(project.toString());
            stbuilder.append(" - ");
            stbuilder.append(project.getDescription() + "\n");
        }

        return stbuilder.toString();
    }

    /*
    * Parse the parameters received into a single one String
    * @param String[] parameters
    * @returns the formatted string
    */
    private String parseParameters(String... parameters) {
        String result = "";
        for (int i = 0; i < parameters.length; i++) {
            if (parameters.length > i + 1) {
                result += parameters[i] + " ";
            } else {
                result += parameters[i];
            }
        }
        return result.toLowerCase();
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