package br.com.jbugbrasil;

import br.com.jbugbrasil.bot.JBugBrasilBot;
import br.com.jbugbrasil.commands.faq.FaqPropertiesLoader;
import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.database.DatabaseComponent;
import br.com.jbugbrasil.exceptions.ParameterNotFoundException;
import br.com.jbugbrasil.gitbooks.impl.GitBooksImpl;
import br.com.jbugbrasil.scheduler.SchedulerComponent;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

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

        // required parameters not found?, stop here.
        if (System.getProperty("br.com.jbugbrasil.telegram.token").length() != 45 || !System.getProperty("br.com.jbugbrasil.telegram.userId").endsWith("_bot") ||
                !System.getProperty("br.com.jbugbrasil.telegram.chatId").startsWith("-") || System.getProperty("br.com.jbugbrasil.gitbooks.token").length() != 64) {

            throw new ParameterNotFoundException("Oops, algum parâmetro obrigatório para a inicialização não encontrado, utilize:\n" +
                    " -Dbr.com.jbugbrasil.telegram.token=<token>\n" +
                    " -Dbr.com.jbugbrasil.telegram.userId=<userId>\n" +
                    " -Dbr.com.jbugbrasil.telegram.chatId=<chatId>\n" +
                    " -Dbr.com.jbugbrasil.gitbooks.token=<OauthToken>");
        }

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        List<Component> components = new ArrayList<>();

        components.add(new FaqPropertiesLoader());
        components.add(new GitBooksImpl());
        components.add(new SchedulerComponent(scheduler));
        components.add(new DatabaseComponent());

        components.forEach(Component::initialize);

        log.info("Schedulers iniciados com sucesso.");
        scheduler.start();

        //start the bot itself
        try {
            ApiContextInitializer.init();
            bot.registerBot(new JBugBrasilBot());
            log.info(BotConfig.JBUG_BRASIL_BOT_USER + " iniciado com sucesso.");
        } catch (TelegramApiException e) {
            log.severe("Falha ao registrar o Bot: " + e.getCause());
        }
    }
}