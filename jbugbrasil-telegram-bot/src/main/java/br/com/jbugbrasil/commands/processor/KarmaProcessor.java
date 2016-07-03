package br.com.jbugbrasil.commands.processor;

import br.com.jbugbrasil.cache.listeners.KarmaEventListener;
import br.com.jbugbrasil.cache.CacheProviderImpl;
import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.database.DatabaseProvider;
import br.com.jbugbrasil.database.impl.DatabaseProviderImpl;
import org.infinispan.Cache;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class KarmaProcessor implements MessageProcessor {

    private final Logger log = Logger.getLogger(KarmaProcessor.class.getName());

    private final Pattern FULL_MSG_PATTERN = Pattern.compile("(\\w*)(\\+\\+|\\-\\-)(\\s|$)");
    private final Pattern KARMA_PATTERN = Pattern.compile("(^\\S*)(\\+\\+|\\-\\-)($)");

    private final DatabaseProvider db = new DatabaseProviderImpl();
    private final CacheProviderImpl cache = CacheProviderImpl.getInstance();
    private final Cache <Object, Integer> c = cache.getCache();


    @Override
    public SendMessage process(Update update) {

        SendMessage echoMessage = new SendMessage();
        StringBuilder message = new StringBuilder();

        if (canProcess(update.getMessage().getText())) {
            echoMessage.setChatId(update.getMessage().getChatId().toString());
            String []str = update.getMessage().getText().split(" ");
            for (int i = 0; i < str.length; i++) {

                if (KARMA_PATTERN.matcher(str[i]).find()){
                    // Get the karma operator
                    String operator = str[i].substring(str[i].length()-2);
                    // Get the username to increase/decrease karma points
                    String username = str[i].substring(0, str[i].length()-2);
                    // Process the karma message
                    processKarma(operator, username);
                    // Add the updated karma in the response message
                    message.append(String.format(BotConfig.KARMA_MESSAGE, username, c.get(username)));
                }
            }
            echoMessage.setText(String.valueOf(message));

        }
        return echoMessage;
    }


    private void processKarma (String operator, String username) {

        cache.getCache().addListener(new KarmaEventListener());
        int atual = getKarma(username);

        if (atual == 0) {
            log.info(username + "não está no cache, colocando");
            c.put(username, atual);
        } else {
            c.put(username, atual);
        }

        switch (operator) {
            case "++":
                c.replace(username, increase(atual));
                break;
            case "--":

                c.replace(username, decrease(atual));
                break;
            default:
                //do nothing
                break;
        }
    }

    private int getKarma (String username) {

        int karma = 0;
        try {
            Statement stmt = db.getConnection().createStatement();
            ResultSet select = stmt.executeQuery("SELECT * FROM KARMA where username='" + username + "'");

            while(select.next()) {
                karma = select.getInt("points");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return karma;
    }

    private int increase (int atual){
        return ++atual;
    }

    private int decrease (int atual){
        return --atual;
    }

    @Override
    public SendMessage reply(SendMessage message) {
        return null;
    }

    @Override
    public boolean canProcess(String messageContent) {
        log.fine("Can process karma? " + messageContent + " " + FULL_MSG_PATTERN.matcher(messageContent).find());
        return FULL_MSG_PATTERN.matcher(messageContent).find();
    }
}