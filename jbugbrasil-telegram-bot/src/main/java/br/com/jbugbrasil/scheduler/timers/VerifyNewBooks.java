package br.com.jbugbrasil.scheduler.timers;

import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.database.DatabaseOperations;
import br.com.jbugbrasil.database.impl.DatabaseProviderImpl;
import br.com.jbugbrasil.gitbooks.GitBooks;
import br.com.jbugbrasil.gitbooks.impl.GitBooksImpl;
import br.com.jbugbrasil.utils.message.impl.MessageSender;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.telegram.telegrambots.api.methods.send.SendMessage;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class VerifyNewBooks implements Job {

    private final GitBooks gitbooks = new GitBooksImpl();
    private DatabaseOperations db = new DatabaseProviderImpl();
    private final SendMessage echoMessage = new SendMessage();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (gitbooks.verifyNewBook() > db.getAmoutOfBooks()) {
            echoMessage.setChatId(BotConfig.DEFAULT_CHAT_ID);
            echoMessage.enableMarkdown(true);
            echoMessage.setText("[Um novo livro foi adicionado no gitbooks](" + BotConfig.GIT_BOOKS_URL +")");
            MessageSender msg = new MessageSender(echoMessage);
            msg.send();
        }
    }
}