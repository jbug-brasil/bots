package br.com.jbugbrasil.conf;

import br.com.jbugbrasil.emojis.Emoji;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public interface BotConfig {

    String JBUG_BRASIL_BOT_TOKEN = System.getProperty("br.com.jbugbrasil.telegram.token");
    String JBUG_BRASIL_BOT_USER = System.getProperty("br.com.jbugbrasil.telegram.userId");
    String DEFAULT_CHAT_ID = System.getProperty("br.com.jbugbrasil.telegram.chatId");

    //Default Messages
    String WELCOME_MESSAGE = "Olá %s, seja bem vindo(a) ao grupo %s. Aqui você poderá discutir sobre" +
            " os projetos opensource da família JBoss e também outros projetos open source. Para conhecer o bot, /help. " + Emoji.SMILING_FACE_WITH_OPEN_MOUTH;
    String GOODBYE_MESSAGE = "Tínhamos um traidor entre nós, %s nos deixou. " + Emoji.ANGRY_FACE;
    String KARMA_MESSAGE = "<b>%s</b> tem <b>%d</b> pontos de karma\n";
    String KARMA_NOT_ALLOWED_MESSAGE = "Ooooops, seja paciente, você já alterou o karma do jovem <b>%s</b>, aguarde 30 segundos. " + Emoji.CONFOUNDED_FACE;
    String GET_KARMA_RESPONSE = "<b>%s</b> possui <b>%s</b> ponto(s) de karma " + Emoji.THUMBS_UP_SIGN;

    //GitBooks configuration
    String GIT_BOOKS_URL = "https://www.gitbook.com/@jboss-books";
    String GIT_BOOKS_ENDPOINT = "https://api.gitbook.com/books";
    String GIT_BOOKS_USER_TOKEN = System.getProperty("br.com.jbugbrasil.gitbooks.token");

    //Faq cofiguration
    String PROJECT_NOT_FOUND_MESSAGE = "Ooops, não encontrei nenhum projeto com o nome %s. " + Emoji.DISAPPOINTED_FACE;

    //Timers interval configuration (in seconds)
    int GIT_BOOKS_CACHE_POPULATOR_INTERVAL = 3600; // 1 hour
    int GIT_BOOKS_VERIFY_NEW_BOOK_INTERVAL = 3600; // 1 hour
    int GIT_BOOKS_VERIFY_BOOK_UPDATE_INTERVAL = 1800; // 1/2 hour
    int FAQ_UPDATE_INTERVAL = 86400; //1 day

}