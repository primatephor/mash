package org.mash.metrics;

import org.apache.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Timed metrics have a maximum run time before stopping.  Once stopped, they're added to a bad
 * list in the manager.  This class has intimate knowledge of the MetricsManager.
 *
 * If the TimedMetrics is ended normally, it's added to the regular group.
 */
public class TimedMetrics extends BaseMetrics
{
    private static final Logger log = Logger.getLogger(TimedMetrics.class.getName());
    private boolean ended = false;
    public static long CULL_TIME = 600000; // 10 minutes maximum run time
    private MetricsManager manager;
    private Timer timer;

    public TimedMetrics(String entity, MetricsManager manager)
    {
        super(entity);
        this.manager = manager;
        startTimer();
    }

    public void startTimer()
    {
        if (timer == null)
        {
            timer = TimerSingleton.getInstance().getTimer();
        }

        if (timer != null)
        {
            timer.schedule(new EndTask(), CULL_TIME);
        }
    }

    @Override
    public Long end()
    {
        Long end = getEnd();
        try
        {
            if (!ended)
            {
                end = super.end();
                //only add stuff less than CULL_TIME
                //assuming there's something very wrong
                //will be reported separately for analysis
                if (end < CULL_TIME)
                {
                    manager.add(this, MetricsManager.getRegular());
                }
                else
                {
                    manager.add(this, MetricsManager.getBad());
                }
            }
        }
        catch (Throwable t)
        {
            log.error("Problem ending stats gathering for " + getEntity(), t);
        }
        ended = true;
        return end;
    }

    private class EndTask extends TimerTask
    {
        @Override
        public void run()
        {
            if (!ended)
            {
                log.info("Stats didn't end by " + CULL_TIME);
                end();
            }
        }
    }
}
