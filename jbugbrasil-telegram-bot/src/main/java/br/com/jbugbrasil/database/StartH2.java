package br.com.jbugbrasil.database;

import org.h2.tools.Server;

import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class StartH2 {

    private static Logger log = Logger.getLogger(StartH2.class.getName());

    private static Server server = null;

    public static void startDatabase() {
        try {
            server = Server.createPgServer().start();
            log.info("Banco de dados iniciado com sucesso;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}