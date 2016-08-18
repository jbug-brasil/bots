package br.com.jbugbrasil.commands.processor;

import br.com.jbugbrasil.cache.CacheProviderImpl;
import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.database.DatabaseOperations;
import br.com.jbugbrasil.database.impl.DatabaseProviderImpl;
import br.com.jbugbrasil.emojis.Emoji;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class KarmaProcessor implements MessageProcessor {

    private final Logger log = Logger.getLogger(KarmaProcessor.class.getName());

    private final Pattern FULL_MSG_PATTERN = Pattern.compile("(\\w*)(\\+\\+|\\-\\-)(\\s|$)");
    private final Pattern KARMA_PATTERN = Pattern.compile("(^\\S+)(\\+\\+|\\-\\-)($)");

    private final DatabaseOperations db = new DatabaseProviderImpl();
    private final CacheProviderImpl cache = CacheProviderImpl.getInstance();


    @Override
    public SendMessage process(Update update) {

        SendMessage echoMessage = new SendMessage();
        StringBuilder message = new StringBuilder();

        if (canProcess(update.getMessage().getText())) {
            echoMessage.setChatId(update.getMessage().getChatId().toString());
            String[] str = update.getMessage().getText().split(" ");

            for (int i = 0; i < str.length; i++) {

                if (KARMA_PATTERN.matcher(str[i]).find()) {
                    // Get the karma operator
                    String operator = str[i].substring(str[i].length() - 2);
                    // Get the username to increase/decrease karma points
                    String target = str[i].substring(0, str[i].length() - 2).toLowerCase();
                    // Process the karma message
                    String username = update.getMessage().getFrom().getUserName() != null ? update.getMessage().getFrom().getUserName() : update.getMessage().getFrom().getFirstName().toLowerCase();
                    message.append(processKarma(operator, target, username));
                }
            }
            echoMessage.setText(String.valueOf(message));

        }
        return echoMessage;
    }

    private String processKarma(String operator, String target, String from) {
        log.fine("username [" + target + "] ---- from[" + from + "]");
        if (from.equals(target)) {
            return "Ooops, querendo alterar seu próprio karma? " + Emoji.DIZZY_FACE;
        }

        //freaking the karma out? stop here.
        if (cache.getCache().containsKey(from + ":" + target)) {
            return String.format(BotConfig.KARMA_NOT_ALLOWED_MESSAGE, target);
        }

        int atual = db.getKarmaPoints(target);
        if (atual == 0) {
            log.info(target + " ainda não possui pontos de karma, criando entrada no cache.");
            cache.getCache().put(target, atual);
        } else {
            cache.getCache().put(target, atual);
        }

        switch (operator) {
            case "++":
                cache.getCache().replace(target, increase(atual));
                cache.getCache().put(from + ":" + target, 0, 30, TimeUnit.SECONDS);
                break;
            case "--":
                cache.getCache().replace(target, decrease(atual));
                cache.getCache().put(from + ":" + target, 0, 30, TimeUnit.SECONDS);
                break;
            default:
                //do nothing
                break;
        }

        return String.format(BotConfig.KARMA_MESSAGE, target, cache.getCache().get(target));
    }

    private int increase(int atual) {
        return ++atual;
    }

    private int decrease(int atual) {
        return --atual;
    }

    @Override
    public boolean canProcess(String messageContent) {
        log.fine("Can process karma? " + messageContent + " " + FULL_MSG_PATTERN.matcher(messageContent).find());
        return FULL_MSG_PATTERN.matcher(messageContent).find();
    }
}