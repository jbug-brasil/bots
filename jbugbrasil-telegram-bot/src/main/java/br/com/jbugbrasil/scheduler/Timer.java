package br.com.jbugbrasil.scheduler;


import org.quartz.SchedulerException;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public interface Timer {

    /*
    * Schedule a new job
    */
    void schedule() throws SchedulerException;

}