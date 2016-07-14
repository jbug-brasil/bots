package br.com.jbugbrasil.database;

import java.sql.Connection;
import java.sql.Statement;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public interface DatabaseOperations {

    Connection getConnection ();

    void closeConnection ();

    void createTableKarma ();

    int getKarmaPoints(String username);
}