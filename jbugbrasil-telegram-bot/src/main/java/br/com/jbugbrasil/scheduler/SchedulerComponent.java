package br.com.jbugbrasil.scheduler;

import br.com.jbugbrasil.exceptions.BotException;
import br.com.jbugbrasil.Component;
import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.scheduler.impl.DefaultTimer;
import br.com.jbugbrasil.scheduler.timers.GitBooksCachePopulator;
import br.com.jbugbrasil.scheduler.timers.UpdateFaqList;
import br.com.jbugbrasil.scheduler.timers.VerifyBookUpdate;
import br.com.jbugbrasil.scheduler.timers.VerifyNewBooks;
import org.h2.command.dml.Update;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

public class SchedulerComponent implements Component {

    private final Scheduler scheduler;

    public SchedulerComponent(Scheduler scheduler) {
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

        // Verify update in any public book
        DefaultTimer verifyBookUpdates = new DefaultTimer(scheduler, VerifyBookUpdate.class,
                "VerifyBookUpdate", "getBooks", "VerifyBookUpdate", "getBooks", BotConfig.GIT_BOOKS_VERIFY_BOOK_UPDATE_INTERVAL);

        // update the faq projects list
        DefaultTimer updateFaqList = new DefaultTimer(scheduler, UpdateFaqList.class,
                "updateFaqList", "faq", "updateFaqList", "faq", BotConfig.FAQ_UPDATE_INTERVAL);

        try {
            gitBooksCachePopulator.schedule();
            verifyNewBook.schedule();
            verifyBookUpdates.schedule();
            updateFaqList.schedule();
        } catch (SchedulerException e) {
            throw new BotException(e.getMessage(), e);
        }
    }
}