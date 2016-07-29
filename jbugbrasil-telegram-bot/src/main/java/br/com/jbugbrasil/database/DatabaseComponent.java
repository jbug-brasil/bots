package br.com.jbugbrasil.database;

import br.com.jbugbrasil.BotException;
import br.com.jbugbrasil.Component;
import org.h2.tools.Server;

import java.sql.SQLException;

public class DatabaseComponent implements Component {

  @Override
  public void initialize() {
    try {
      Server.createPgServer().start();
    } catch (SQLException e) {
      throw new BotException(e.getMessage(), e);
    }
  }

}
