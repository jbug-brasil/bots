package br.com.jbugbrasil.scheduler.timers;

import br.com.jbugbrasil.cache.CacheProviderImpl;
import br.com.jbugbrasil.database.DatabaseOperations;
import br.com.jbugbrasil.database.impl.DatabaseProviderImpl;
import br.com.jbugbrasil.gitbooks.GitBooks;
import br.com.jbugbrasil.gitbooks.impl.GitBooksImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class GitBooksCachePopulator implements Job {

    private final GitBooks gitbooks = new GitBooksImpl();
    private final CacheProviderImpl cache = CacheProviderImpl.getInstance();
    private final SendMessage echoMessage = new SendMessage();
    private Logger log = Logger.getLogger(GitBooksCachePopulator.class.getName());
    private DatabaseOperations db = new DatabaseProviderImpl();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //get the amount of books, update the cache and the database
        putAmountOfBooks("amount", gitbooks.verifyNewBook());

    }

    /*
    * Update the amountOfTheBooks in the cache and database.
    * @param Object key
    * @param Object value
    */
    private void putAmountOfBooks(Object key, Object value) {
        if (!cache.getCache().containsKey(key)) {
            log.fine("CACHE NÂO CONTEM A CHAVE " + key + ". ADICIONANDO " + key + ":" + value);
            cache.getCache().put(key, value);
            db.setAmountOfBooks(Integer.parseInt(value.toString()));
        } else {
            if (!cache.getCache().get(key).equals(value)) {
                log.fine("VALOR DO AMOUNT OF BOOKS ATUALIZADO. ATUALIZANDO CACHE.");
                cache.getCache().replace(key, value);
                db.setAmountOfBooks(Integer.parseInt(value.toString()));
            }
        }
    }
}