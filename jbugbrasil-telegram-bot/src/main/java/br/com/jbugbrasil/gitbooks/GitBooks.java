package br.com.jbugbrasil.gitbooks;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public interface GitBooks {

    /*
    * Get all books available in the gitbooks page
    */
    String getBooks();

    /*
    * Verifies if a new book were added on gitbooks
    */
    int verifyNewBook();

    /*
    * To be implemented
    */
    String verifyUpdates();
}