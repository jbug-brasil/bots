package br.com.jbugbrasil.gitbooks;

import br.com.jbugbrasil.gitbooks.pojo.Books;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public interface GitBooks {

    /*
    * Get all books available in the gitbooks page
    */
    List<Books> getBooks() throws IOException;

    /*
    * Verifies if a new book were added on gitbooks
    */
    int verifyNewBook();

    /*
    * Perform a request againts jboss-books main rest api and put the JSON in the cache
    */
    void initialize() throws IOException;
}