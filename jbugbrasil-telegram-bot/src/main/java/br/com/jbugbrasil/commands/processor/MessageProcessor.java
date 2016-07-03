package br.com.jbugbrasil.commands.processor;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public interface MessageProcessor {

    SendMessage process (Update update);

    SendMessage reply (SendMessage message);

    boolean canProcess (String messageContent);
}