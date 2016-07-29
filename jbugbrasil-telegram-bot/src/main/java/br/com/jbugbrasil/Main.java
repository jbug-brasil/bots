package br.com.jbugbrasil;

import br.com.jbugbrasil.bot.JBugBrasilBot;
import br.com.jbugbrasil.commands.faq.FaqPropertiesLoader;
import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.database.DatabaseComponent;
import br.com.jbugbrasil.scheduler.GitBooksSchedulerComponent;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Ingo
 * @author fspolti
 * @author ataxexe
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    private static final TelegramBotsApi bot = new TelegramBotsApi();

    public static void main(String[] args) throws Exception {
        // token not found, stop here.
        if (System.getProperty("br.com.jbugbrasil.telegram.token").length() != 45) {
            throw new IllegalArgumentException("Token não encontrado ou inválido, utilize" +
                " -Dbr.com.jbugbrasil.telegram.token=<token>");
        }

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        List<Component> components = new ArrayList<>();
        components.add(new DatabaseComponent());
        components.add(new FaqPropertiesLoader());
        components.add(new GitBooksSchedulerComponent(scheduler));

        components.forEach(Component::initialize);

        log.info("Schedulers iniciados com sucesso.");
        scheduler.start();
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