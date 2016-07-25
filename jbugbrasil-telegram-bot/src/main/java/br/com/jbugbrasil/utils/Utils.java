package br.com.jbugbrasil.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.time.Duration;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class Utils {

    /*
    * Returns the uptime in the following pattern: 0 Hora(s), 1 minuto(s) e 1 segundo(s).
    * Suggested by Ingo
    */
    public static String upTime() throws ParseException {
        Duration duration = Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long seconds = duration.minusHours(hours).minusMinutes(minutes).getSeconds();
        String result = hours + " Hora(s), " + minutes + " Minuto(s) e " + seconds + " Segundo(s)";
        return result;
    }

}