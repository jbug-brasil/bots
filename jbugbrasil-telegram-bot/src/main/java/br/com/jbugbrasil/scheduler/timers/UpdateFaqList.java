package br.com.jbugbrasil.scheduler.timers;

import br.com.jbugbrasil.commands.faq.FaqPropertiesLoader;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class UpdateFaqList implements Job {

    private final Logger log = Logger.getLogger(UpdateFaqList.class.getName());
    private final FaqPropertiesLoader loader = new FaqPropertiesLoader();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Updating the faq project lists");
        loader.initialize();
    }
}