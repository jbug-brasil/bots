package br.com.jbugbrasil;

import br.com.jbugbrasil.bot.WFlyBRBot;
import br.com.jbugbrasil.cache.CacheProviderImpl;
import br.com.jbugbrasil.conf.BotConfig;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.wildfly.swarm.container.Container;

import java.util.logging.Logger;

/**
 * @author Ingo
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {

        //Default cache manager, start it in the startup
        CacheProviderImpl cache = CacheProviderImpl.getInstance();

        TelegramBotsApi bots = new TelegramBotsApi();

        if (System.getProperty("br.com.jbugbrasil.telegram.token").length() != 45) {
            throw new IllegalArgumentException("Token não encontrado ou inválido, utilize -Dbr.com.jbugbrasil.telegram.token=<token>");
        }

        try {
            bots.registerBot(new WFlyBRBot());
            log.info("wflybr_bot iniciado com sucesso.");
        } catch (TelegramApiException e) {
            log.severe("Falha ao registrar o Bot: " + e.getCause());
        }
    }
}