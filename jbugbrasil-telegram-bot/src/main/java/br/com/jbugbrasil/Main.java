package br.com.jbugbrasil;

import br.com.jbugbrasil.bot.JBugBrasilBot;
import br.com.jbugbrasil.conf.BotConfig;
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

        // token not found, stop here.
        if (System.getProperty("br.com.jbugbrasil.telegram.token").length() != 45) {
            throw new IllegalArgumentException("Token não encontrado ou inválido, utilize -Dbr.com.jbugbrasil.telegram.token=<token>");
        }

        //Start Database
        startDatabase();

        //Start the timers
//        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        DefaultTimer timer = new DefaultTimer(scheduler, MyNewTimer.class,"verifyNewBooks", "getBooks", "getBooksTrigger", "getBooks", 5);
//        scheduler.start();
//        timer.schedule();
//        log.info("Scheduler iniciado com sucesso.");

        //start the bot itself
        try {
            bot.registerBot(new JBugBrasilBot());
            log.info(BotConfig.JBUG_BRASIL_BOT_USER + " iniciado com sucesso.");
        } catch (TelegramApiException e) {
            log.severe("Falha ao registrar o Bot: " + e.getCause());
        }
    }
}