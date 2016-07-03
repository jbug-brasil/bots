package br.com.jbugbrasil.cache.listeners;

import br.com.jbugbrasil.database.DatabaseProvider;
import br.com.jbugbrasil.database.impl.DatabaseProviderImpl;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Listener
public class KarmaEventListener {

    private final Logger log = Logger.getLogger(KarmaEventListener.class.getName());
    private DatabaseProvider db = new DatabaseProviderImpl();

    private int count = 0;

    @CacheEntryCreated
    public void entryCreated(CacheEntryCreatedEvent event) {
        if (event.getValue() != null) {
            updateKarma(event.getKey().toString(), Integer.parseInt(event.getValue().toString()));
        }
    }

    @CacheEntryModified
    public void entryModified(CacheEntryModifiedEvent event) {
        updateKarma(event.getKey().toString(), Integer.parseInt(event.getValue().toString()));
    }

    @CacheEntryRemoved
    public void entryRemoved(CacheEntryRemovedEvent event) {
        log.info("entry " + event.getKey() + " removed from the cache");
    }

    /*
    * The methods are being called more than 1 time, let's limit it to only one time
    */
    private boolean isFirst(int count) {
        return count == 1 ? true : false;
    }

    private void updateKarma(String username, int points) {
        //Create needed tables if it does not exist
        db.createTable();

        try {

            Statement stmt = db.getConnection().createStatement();

            //update or create the karma reference in the database
            ResultSet select = stmt.executeQuery("SELECT * FROM KARMA where username='" + username + "'");
            if (select.next()) {
                stmt.executeUpdate("UPDATE KARMA SET points=" + points + " WHERE username='" + username + "'");

            } else {
                stmt.executeUpdate("INSERT INTO KARMA ( username, points ) VALUES ( '" + username + "' , " + points + ")");
            }

            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}