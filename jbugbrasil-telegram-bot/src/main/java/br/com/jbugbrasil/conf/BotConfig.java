package br.com.jbugbrasil.conf;

import br.com.jbugbrasil.emojis.Emoji;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class BotConfig {

    public static final String WFLYBR_TOKEN = System.getProperty("br.com.jbugbrasil.telegram.token");
    public static final String WFLYBR_USER = "jbugbrasil_bot";

    //Default Messages
    public static final String WELCOME_MESSAGE = " Olá %s, seja bem vindo ao grupo WildFly BR. Aqui você poderá discutir sobre" +
            " os projetos opensource da família JBoss. Para utilizar o bot, começe digitando /help. " + Emoji.SMILING_FACE_WITH_OPEN_MOUTH;
    public static final String GOODBYE_MESSAGE = "Tínhamos um traidor entre nós, %s nos deixou. " + Emoji.ANGRY_FACE;
    public static final String KARMA_MESSAGE = "%s tem %d pontos de karma\n";
    public static final String KARMA_NOT_ALLOWED_MESSAGE = " Ooooops, seja paciente, você já alterou o karma de <b>%s<b>, aguarde 30 segundos. "  + Emoji.CONFOUNDED_FACE;
    public static final String GET_KARMA_RESPONSE = "%s possui %s ponto(s) de karma " + Emoji.THUMBS_UP_SIGN;

}