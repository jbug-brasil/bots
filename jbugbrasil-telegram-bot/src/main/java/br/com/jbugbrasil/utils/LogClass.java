package br.com.jbugbrasil.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class LogClass {

    private static final Logger log = Logger.getLogger(LogClass.class.getName());
    private static final String LOGGING_PROPERTIES_FILE = "/META-INF/logging.properties";

    /*
    * Static loader
    */
    static {
        final LogManager logManager = LogManager.getLogManager();
        try (final InputStream is = LogClass.class.getResourceAsStream(LOGGING_PROPERTIES_FILE)) {
            logManager.readConfiguration(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}