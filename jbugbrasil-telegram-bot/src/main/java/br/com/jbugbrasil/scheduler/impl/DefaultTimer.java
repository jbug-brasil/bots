package br.com.jbugbrasil.scheduler.impl;

import br.com.jbugbrasil.scheduler.Timer;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class DefaultTimer implements Timer {

    private Scheduler scheduler;
    private Class jobClazz;
    private String jobName;
    private String jobGroup;
    private String triggerName;
    private String triggerGroup;
    private int interval;

    public DefaultTimer(Scheduler scheduler, Class jobClazz, String jobName, String jobGroup, String triggerName, String triggerGroup, int interval) {
        this.scheduler = scheduler;
        this.jobClazz = jobClazz;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.interval = interval;
    }

    @Override
    public void schedule() throws SchedulerException {

        JobDetail job = JobBuilder.newJob(this.jobClazz)
                .withIdentity(this.jobName, this.jobGroup)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(this.triggerName, this.triggerGroup)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(this.interval)
                        .repeatForever())
                .build();

        scheduler.scheduleJob(job, trigger);
    }
}