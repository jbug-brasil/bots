package br.com.jbugbrasil;

import br.com.jbugbrasil.bot.JBugBrasilBot;
import br.com.jbugbrasil.commands.faq.FaqPropertiesLoader;
import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.scheduler.impl.DefaultTimer;
import br.com.jbugbrasil.scheduler.timers.GitBooksCachePopulator;
import br.com.jbugbrasil.scheduler.timers.VerifyNewBooks;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

import java.util.logging.Logger;

import static br.com.jbugbrasil.database.StartH2.startDatabase;

/**
 * @author Ingo
 * @author fspolti
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    private static final TelegramBotsApi bot = new TelegramBotsApi();

    public static void main(String[] args) throws Exception {

        //Read the projects from json file then send it to the cache.
        FaqPropertiesLoader.load();

        // token not found, stop here.
        if (System.getProperty("br.com.jbugbrasil.telegram.token").length() != 45) {
            throw new IllegalArgumentException("Token não encontrado ou inválido, utilize -Dbr.com.jbugbrasil.telegram.token=<token>");
        }

        //Start Database
        startDatabase();

        //Start the timers
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        //Start the cache populator responsible to get the books's information on gitbooks, interval of one hour 3600
        DefaultTimer gitBooksCachePopulator = new DefaultTimer(scheduler, GitBooksCachePopulator.class,
                "GitBooksCachePopulatorJob", "getBooks", "GitBooksCachePopulatorJobTrigger", "getBooks", BotConfig.GIT_BOOKS_CACHE_POPULATOR_INTERVAL);
        //Verify if a new book were added on gitbooks
        DefaultTimer verifyNewBook = new DefaultTimer(scheduler, VerifyNewBooks.class,
                "VerifyNewBooks", "getBooks", "VerifyNewBooks", "getBooks", BotConfig.GIT_BOOKS_VERIFY_NEW_BOOK_INTERVAL);
        scheduler.start();
        gitBooksCachePopulator.schedule();
        verifyNewBook.schedule();
        log.info("Schedulers iniciados com sucesso.");
        //////////////////////////////////

        //start the bot itself
        try {
            bot.registerBot(new JBugBrasilBot());
            log.info(BotConfig.JBUG_BRASIL_BOT_USER + " iniciado com sucesso.");
        } catch (TelegramApiException e) {
            log.severe("Falha ao registrar o Bot: " + e.getCause());
        }
    }
}