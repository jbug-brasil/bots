package br.com.jbugbrasil.scheduler.timers;

import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.gitbooks.GitBooks;
import br.com.jbugbrasil.gitbooks.impl.GitBooksImpl;
import br.com.jbugbrasil.utils.message.impl.MessageSender;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.telegram.telegrambots.api.methods.send.SendMessage;

/**
 * Created by fspolti on 7/22/16.
 */
public class VerifyNewBooks implements Job {

    private final GitBooks gitbooks = new GitBooksImpl();
    private final SendMessage echoMessage = new SendMessage();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        echoMessage.setChatId(BotConfig.DEFAULT_CHAT_ID);
        echoMessage.setText("Um novo livro foi adicionado no gitbooks");

        MessageSender msg = new MessageSender(echoMessage);
        msg.send();

    }
}