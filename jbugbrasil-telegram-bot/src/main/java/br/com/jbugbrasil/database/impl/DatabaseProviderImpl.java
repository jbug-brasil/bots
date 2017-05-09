package br.com.jbugbrasil.database.impl;

import br.com.jbugbrasil.database.DatabaseOperations;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class DatabaseProviderImpl implements DatabaseOperations {

    private final Logger log = Logger.getLogger(DatabaseProviderImpl.class.getName());

    private final String DB_DRIVER = "org.h2.Driver";
    private final String DB_URL = "jdbc:h2:file:/opt/h2/bot.db";
    private final String DB_USER = "bot";
    private final String DB_PASSWORD = "bot";

    private Connection connection;

    public DatabaseProviderImpl() {
    }

    @Override
    public Connection getConnection() {

        if (connection == null) {
            try {
                Class.forName(DB_DRIVER);
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            } catch (SQLException e) {
                log.severe("Erro ao criar conexão: " + DB_DRIVER);

            } catch (ClassNotFoundException e) {
                log.severe(" Classe não encontrada: " + DB_DRIVER);
            }
        }
        return connection;
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            log.warning("Falha ao fechar conexão: " + e.getCause());
        }
    }

    @Override
    public void createTableKarma() {

        try {
            DatabaseMetaData md = getConnection().getMetaData();
            ResultSet table = md.getTables(null, null, "KARMA", null);

            if (table.next()) {
                // do nothing
            } else {
                Statement stmt = getConnection().createStatement();
                stmt.executeUpdate("CREATE TABLE KARMA ( username varchar(50), points int )");
                stmt.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getKarmaPoints(String username) {

        int karma = 0;
        try {
            Statement stmt = getConnection().createStatement();
            ResultSet select = stmt.executeQuery("SELECT points FROM KARMA where username='" + username + "'");

            while (select.next()) {
                karma = select.getInt("points");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return karma;

    }

    @Override
    public void createTableAmountOfBooks() {
        try {
            DatabaseMetaData md = getConnection().getMetaData();
            ResultSet table = md.getTables(null, null, "AMOUNTOFBOOKS", null);

            if (table.next()) {
                // do nothing
            } else {
                Statement stmt = getConnection().createStatement();
                stmt.executeUpdate("CREATE TABLE AMOUNTOFBOOKS ( amount int )");
                stmt.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTableBookUpdate() {
        try {
            DatabaseMetaData md = getConnection().getMetaData();
            ResultSet table = md.getTables(null, null, "BOOKUPDATES", null);

            if (table.next()) {
                // do nothing
            } else {
                Statement stmt = getConnection().createStatement();
                stmt.executeUpdate("CREATE TABLE BOOKUPDATES ( bookName varchar(50), updates int )");
                stmt.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setAmountOfBooks(int amount) {
        createTableAmountOfBooks();

        try {

            Statement stmt = getConnection().createStatement();

            //update or create the karma reference in the database
            ResultSet select = stmt.executeQuery("SELECT amount FROM AMOUNTOFBOOKS");
            if (select.next()) {
                stmt.executeUpdate("UPDATE AMOUNTOFBOOKS SET amount=" + amount);
            } else {
                stmt.executeUpdate("INSERT INTO AMOUNTOFBOOKS ( amount ) VALUES ( " + amount + ")");
            }

            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBookUpdate(String bookName, int updates) {
        createTableBookUpdate();

        try {

            Statement stmt = getConnection().createStatement();

            //update or create the karma reference in the database
            ResultSet select = stmt.executeQuery("SELECT bookName FROM BOOKUPDATES where bookName='" + bookName + "'");
            if (select.next()) {
                stmt.executeUpdate("UPDATE BOOKUPDATES SET updates=" + updates + " where bookName='" + bookName + "'");
            } else {
                stmt.executeUpdate("INSERT INTO BOOKUPDATES ( bookName, updates ) VALUES ('" + bookName + "'," + updates + ")");
            }
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getAmoutOfBooks() {
        int amount = 0;
        try {
            Statement stmt = getConnection().createStatement();
            ResultSet select = stmt.executeQuery("SELECT amount FROM AMOUNTOFBOOKS");

            while (select.next()) {
                amount = select.getInt("amount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }

    @Override
    public int getBookUpdates(String bookName) {
        int updates = 0;
        try {
            Statement stmt = getConnection().createStatement();
            ResultSet select = stmt.executeQuery("SELECT updates FROM BOOKUPDATES where bookName='" + bookName + "'");

            while (select.next()) {
                updates = select.getInt("updates");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updates;
    }
}