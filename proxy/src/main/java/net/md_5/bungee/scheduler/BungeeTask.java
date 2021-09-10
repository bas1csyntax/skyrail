package net.md_5.bungee.scheduler;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import lombok.Data;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

@Data
public class BungeeTask implements Runnable, ScheduledTask
{

    private final BungeeScheduler sched;
    private final int id;
    private final Plugin owner;
    private final Runnable task;
    //
    private final long delay;
    private final long period;
    private final AtomicBoolean running = new AtomicBoolean( true );

    public BungeeTask(BungeeScheduler sched, int id, Plugin owner, Runnable task, long delay, long period, TimeUnit unit)
    {
        this.sched = sched;
        this.id = id;
        this.owner = owner;
        this.task = task;
        this.delay = unit.toMillis( delay );
        this.period = unit.toMillis( period );
    }

    @Override
    public void cancel()
    {
        boolean wasRunning = running.getAndSet( false );

        if ( wasRunning )
        {
            sched.cancel0( this );
        }
    }

    @Override
    public void run()
    {
        if ( delay > 0 )
        {
            try
            {
                Thread.sleep( delay );
            } catch ( InterruptedException ex )
            {
                Thread.currentThread().interrupt();
            }
        }

        while ( running.get() )
        {
            try
            {
                task.run();
            } catch ( Throwable t )
            {
                Logger logger = ProxyServer.getInstance().getLogger();
                logger.log( log( t, "Task {0} encountered an exception", new Object[]
                {
                    this
                }, logger ) );
            }

            // If we have a period of 0 or less, only run once
            if ( period <= 0 )
            {
                break;
            }

            try
            {
                Thread.sleep( period );
            } catch ( InterruptedException ex )
            {
                Thread.currentThread().interrupt();
            }
        }

        cancel();
    }

    private static LogRecord log(Throwable t, String msg, Object[] parameters, Logger logger)
    {
        LogRecord logRecord = new LogRecord( Level.SEVERE, msg );
        logRecord.setParameters( parameters );
        logRecord.setThrown( t );
        logRecord.setLoggerName( logger.getName() );
        return logRecord;
    }
}
