package br.com.jbugbrasil.scheduler;

import br.com.jbugbrasil.exceptions.BotException;
import br.com.jbugbrasil.Component;
import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.scheduler.impl.DefaultTimer;
import br.com.jbugbrasil.scheduler.timers.GitBooksCachePopulator;
import br.com.jbugbrasil.scheduler.timers.VerifyNewBooks;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

public class GitBooksSchedulerComponent implements Component {

  private final Scheduler scheduler;

  public GitBooksSchedulerComponent(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  @Override
  public void initialize() {
    //Start the cache populator responsible to get the books's information on gitbooks, interval of one hour 3600
    Timer gitBooksCachePopulator = new DefaultTimer(scheduler, GitBooksCachePopulator.class,
        "GitBooksCachePopulatorJob", "getBooks", "GitBooksCachePopulatorJobTrigger", "getBooks",
        BotConfig.GIT_BOOKS_CACHE_POPULATOR_INTERVAL);
    //Verify if a new book were added on gitbooks
    DefaultTimer verifyNewBook = new DefaultTimer(scheduler, VerifyNewBooks.class,
        "VerifyNewBooks", "getBooks", "VerifyNewBooks", "getBooks", BotConfig.GIT_BOOKS_VERIFY_NEW_BOOK_INTERVAL);
    try {
      gitBooksCachePopulator.schedule();
      verifyNewBook.schedule();
    } catch (SchedulerException e) {
      throw new BotException(e.getMessage(), e);
    }
  }
}