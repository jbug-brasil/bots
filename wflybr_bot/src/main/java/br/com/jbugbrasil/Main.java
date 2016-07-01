package br.com.jbugbrasil;

import br.com.jbugbrasil.bot.WFlyBRBot;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

import java.util.logging.Logger;

/**
 * @author Ingo
 */
public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        TelegramBotsApi bots = new TelegramBotsApi();

        try {
            bots.registerBot(new WFlyBRBot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        log.info("wflybr_bot iniciado com sucesso.");
    }

}
