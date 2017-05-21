package br.com.jbugbrasil.commands.faq;

import br.com.jbugbrasil.cache.CacheProviderImpl;
import br.com.jbugbrasil.commands.Commands;
import br.com.jbugbrasil.commands.processor.MessageProcessor;
import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.utils.Utils;
import br.com.jbugbrasil.utils.message.impl.MessageSender;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class FaqCommand extends BotCommand implements Commands, MessageProcessor {

    private static final Logger log = Logger.getLogger(FaqPropertiesLoader.class.getName());

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
        faqMessage.enableHtml(true);

        if (strings.length == 0) {
            faqMessage.setText("<b>faq</b>\nParametro é obrigatório");
        } else {
            faqMessage.setText(Utils.prepareString(query(parseParameters(strings))));
        }

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

        // get all Project type items from cache
        List<Project> cacheEntries = (List<Project>) cache.getCache().values().stream()
                .filter(item -> item instanceof Project)
                .collect(Collectors.toList());

        // Filter all elements that matches the given key and append it to the response
        for (Project project : cacheEntries.stream().filter(item -> item.getId().contains(key)).collect(Collectors.toList())) {
            stbuilder.append(project.toString());
            stbuilder.append(" - ");
            stbuilder.append(project.getDescription() + "\n");
        }

        if (stbuilder.length() <= 0) {
            stbuilder.append(String.format(BotConfig.PROJECT_NOT_FOUND_MESSAGE, key));
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