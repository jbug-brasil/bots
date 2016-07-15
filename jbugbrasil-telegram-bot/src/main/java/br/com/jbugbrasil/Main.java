package br.com.jbugbrasil;

import br.com.jbugbrasil.bot.JBugBrasilBot;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

import java.util.logging.Logger;

import static br.com.jbugbrasil.database.StartH2.startDatabase;

/**
 * @author Ingo
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {

        //Start Database
        startDatabase();

        TelegramBotsApi bots = new TelegramBotsApi();

        if (System.getProperty("br.com.jbugbrasil.telegram.token").length() != 45) {
            throw new IllegalArgumentException("Token não encontrado ou inválido, utilize -Dbr.com.jbugbrasil.telegram.token=<token>");
        }

        try {
            bots.registerBot(new JBugBrasilBot());
            log.info("jbugbrasil_bot iniciado com sucesso.");
        } catch (TelegramApiException e) {
            log.severe("Falha ao registrar o Bot: " + e.getCause());
        }
    }
}