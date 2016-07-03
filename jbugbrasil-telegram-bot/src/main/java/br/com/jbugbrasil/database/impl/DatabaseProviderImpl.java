package br.com.jbugbrasil.database.impl;

import br.com.jbugbrasil.database.DatabaseProvider;
import org.h2.tools.Server;

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
public class DatabaseProviderImpl implements DatabaseProvider {

    private final Logger log = Logger.getLogger(DatabaseProviderImpl.class.getName());

    private final String DB_DRIVER = "org.h2.Driver";
    private final String DB_URL = "jdbc:h2:file:/opt/h2/bot.db";
    private final String DB_USER = "bot";
    private final String DB_PASSWORD = "bot";

    private Connection connection;

    public DatabaseProviderImpl() {}

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
    public void createTable () {

        try {

            DatabaseMetaData md = getConnection().getMetaData();
            ResultSet table = md.getTables(null, null, "KARMA", null);

            if (table.next()) {
                log.info("Tabela KARMA já existe, ignorando a criação");
            } else {
                Statement stmt = getConnection().createStatement();
                stmt.executeUpdate( "CREATE TABLE KARMA ( username varchar(50), points int )" );
                stmt.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}