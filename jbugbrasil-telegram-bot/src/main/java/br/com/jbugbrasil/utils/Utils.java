package br.com.jbugbrasil.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class Utils {

    /*
    * Returns the uptime in minutes.
    */
    public static long upTime() throws ParseException {
        return TimeUnit.MINUTES.convert(ManagementFactory.getRuntimeMXBean().getUptime(), TimeUnit.MILLISECONDS);
    }
}