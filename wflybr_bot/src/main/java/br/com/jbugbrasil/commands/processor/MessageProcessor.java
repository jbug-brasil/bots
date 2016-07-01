package br.com.jbugbrasil.commands.processor;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Created by fspolti on 7/1/16.
 */
public interface MessageProcessor {

    SendMessage process (Update update);

    SendMessage reply (SendMessage message);

    boolean canProcess (String messageContent);
}