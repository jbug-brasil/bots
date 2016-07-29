package br.com.jbugbrasil.commands;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public interface Commands {

    String HELP = "help";
    String GET_BOOKS = "getbooks";
    String GET_KARMA = "getkarma";
    String UPTIME = "uptime";
    String FAQ = "faq";

    //commands with no / as prefix
    String PING = "ping";
    String PONG = "%s pong";

}