package br.com.jbugbrasil.conf;

import br.com.jbugbrasil.emojis.Emoji;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public interface BotConfig {

    String JBUG_BRASIL_BOT_TOKEN = System.getProperty("br.com.jbugbrasil.telegram.token");
    //String JBUG_BRASIL_BOT_USER = "jbugbrasil_bot";
    String DEFAULT_CHAT_ID = "-136511623"; //jbugbrasilbot chat Id: -136511623

    //Default Messages
    String WELCOME_MESSAGE = " Olá %s, seja bem vindo ao grupo %s. Aqui você poderá discutir sobre" +
            " os projetos opensource da família JBoss. Para utilizar o bot, começe digitando /help. " + Emoji.SMILING_FACE_WITH_OPEN_MOUTH;
    String GOODBYE_MESSAGE = "Tínhamos um traidor entre nós, %s nos deixou. " + Emoji.ANGRY_FACE;
    String KARMA_MESSAGE = "%s tem %d pontos de karma\n";
    String KARMA_NOT_ALLOWED_MESSAGE = " Ooooops, seja paciente, você já alterou o karma do username %s, aguarde 30 segundos. "  + Emoji.CONFOUNDED_FACE;
    String GET_KARMA_RESPONSE = "%s possui %s ponto(s) de karma " + Emoji.THUMBS_UP_SIGN;

}