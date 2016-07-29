package br.com.jbugbrasil.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class LogClassLoader {

    private static final Logger log = Logger.getLogger(LogClassLoader.class.getName());
    private static final String LOGGING_PROPERTIES_FILE = "/META-INF/logging.properties";

    /*
    * Static loader
    * Load the logging properties from META-INF dir
    */
    static {
        log.info("Loading the logging configuration file");
        final LogManager logManager = LogManager.getLogManager();
        try (final InputStream input = LogClassLoader.class.getResourceAsStream(LOGGING_PROPERTIES_FILE)) {
            logManager.readConfiguration(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}