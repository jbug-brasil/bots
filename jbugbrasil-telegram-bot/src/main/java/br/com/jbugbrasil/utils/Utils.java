package br.com.jbugbrasil.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class Utils {


    /*
        * Returns the uptime in minutes.
        */
    public static Duration upTime() throws ParseException {
        //return TimeUnit.MINUTES.convert(ManagementFactory.getRuntimeMXBean().getUptime(), TimeUnit.MILLISECONDS);
        return Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());
    }

    public static void main (String args[]) {

        Duration duration = Duration.ofMillis(942342344);
        System.out.println(duration);
//
        System.out.println(duration.toHours());
        System.out.println(duration.toDays());

        Duration newduration = Duration.parse("P2DT3H4M");
        System.out.println(newduration);

    }
}