package br.com.jbugbrasil;

import br.com.jbugbrasil.bot.WFlyBRBot;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */

@Startup
@Singleton
public class RegisterBot  {

    private final Logger log = Logger.getLogger(RegisterBot.class.getName());

    private TelegramBotsApi bots = new TelegramBotsApi();

    @PostConstruct
    public void Startup() {

        try {
            bots.registerBot(new WFlyBRBot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        log.info("wflybr_bot iniciado com sucesso.");
    }

    @PreDestroy
    public void Shutdown() {
        log.info("Shutdown OK");
    }
}