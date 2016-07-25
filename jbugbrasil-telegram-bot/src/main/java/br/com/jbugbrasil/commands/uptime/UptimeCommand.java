package br.com.jbugbrasil.commands.uptime;

import br.com.jbugbrasil.commands.Commands;
import br.com.jbugbrasil.utils.Utils;
import br.com.jbugbrasil.utils.message.impl.MessageSender;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;

import java.text.ParseException;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class UptimeCommand extends BotCommand implements Commands {

    private final ICommandRegistry commandRegistry;

    public UptimeCommand(ICommandRegistry commandRegistry) {
        super(Commands.UPTIME, "Mostra o tempo que o Bot está no ar");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder response = new StringBuilder("*Uptime:* ");

        try {
            response.append("`" + Utils.upTime() + "`.");
            SendMessage uptimeMessage = new SendMessage();
            uptimeMessage.setChatId(chat.getId().toString());
            uptimeMessage.enableMarkdown(true);
            uptimeMessage.setText(response.toString());
            MessageSender msg = new MessageSender(uptimeMessage);
            msg.send();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}