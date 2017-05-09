package br.com.jbugbrasil.database;

import br.com.jbugbrasil.database.impl.DatabaseProviderImpl;
import br.com.jbugbrasil.exceptions.BotException;
import br.com.jbugbrasil.Component;
import br.com.jbugbrasil.gitbooks.GitBooks;
import br.com.jbugbrasil.gitbooks.impl.GitBooksImpl;
import org.h2.tools.Server;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseComponent implements Component {

    private DatabaseOperations db = new DatabaseProviderImpl();
    private final GitBooks gitbooks = new GitBooksImpl();

    @Override
    public void initialize() {
        try {
            Server.createPgServer().start();

            // updating the database with values from gitbooks
            gitbooks.getBooks().stream()
                    .filter(book -> book.isPublic())
                    .forEach(book -> {
                        db.setBookUpdate(book.getName(), book.getCounts().getUpdates());
                    });
        } catch (SQLException e) {
            throw new BotException(e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}