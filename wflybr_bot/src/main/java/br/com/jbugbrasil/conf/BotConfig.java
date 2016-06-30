package br.com.jbugbrasil.conf;

import br.com.jbugbrasil.emojis.Emoji;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class BotConfig {

    //REMOVE THE TOKEN BEFORE COMMIT
    public static final String WFLYBR_TOKEN = "230934125:AAF83ba5EychdWRrUXuD4CRsVMr8Qa3LvlM";
    public static final String WFLYBR_USER = "wflybr_bot";

    //Default Messages
    public static final String WELCOME_MESSAGE = " Olá %s, seja bem vindo ao grupo WildFly BR. Aqui você poderá discutir sobre" +
            " os projetos opensource da família JBoss. Para utulizar o bot, começe digitando /help. " + Emoji.SMILING_FACE_WITH_OPEN_MOUTH;
    public static final String GOODBYE_MESSAGE = "Tínhamos um traidor entre nos, %s nos deixou. " + Emoji.ANGRY_FACE;


}