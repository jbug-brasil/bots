package br.com.jbugbrasil.gitbooks;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public interface GitBooks {

    String getBooks();

    boolean verifyNewBook();

    String verifyUpdates();
}