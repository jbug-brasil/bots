package br.com.jbugbrasil.scheduler.timers;

import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.database.DatabaseOperations;
import br.com.jbugbrasil.database.impl.DatabaseProviderImpl;
import br.com.jbugbrasil.gitbooks.GitBooks;
import br.com.jbugbrasil.gitbooks.impl.GitBooksImpl;
import br.com.jbugbrasil.gitbooks.pojo.Books;
import br.com.jbugbrasil.utils.message.impl.MessageSender;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class VerifyBookUpdate implements Job {

    private final GitBooks gitbooks = new GitBooksImpl();
    private DatabaseOperations db = new DatabaseProviderImpl();
    private final SendMessage echoMessage = new SendMessage();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<Books> publicBooks = gitbooks.getBooks().stream()
                    .filter(book -> book.isPublic())
                    .collect(Collectors.toList());

            publicBooks.stream().forEach(book -> {
                if (book.getCounts().getUpdates() > db.getBookUpdates(book.getName())){
                    echoMessage.setChatId(BotConfig.DEFAULT_CHAT_ID);
                    echoMessage.enableMarkdown(true);
                    echoMessage.setText("O seguinte livro foi atualizado: [ " + book.getName() + "](" + book.getUrls().getRead() +")");
                    db.setBookUpdate(book.getName(), book.getCounts().getUpdates());
                    MessageSender msg = new MessageSender(echoMessage);
                    msg.send();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}