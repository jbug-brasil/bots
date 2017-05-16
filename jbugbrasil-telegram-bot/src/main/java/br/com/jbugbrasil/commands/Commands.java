package br.com.jbugbrasil.commands;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public interface Commands {

    String HELP = "help";
    String GET_BOOKS = "books";
    String GET_KARMA = "karma";
    String UPTIME = "uptime";
    String FAQ = "faq";
    String URBAN_DICTIONARY = "define";

    //commands with no / as prefix
    String PING = "ping";
    String PONG = "%s pong";

}