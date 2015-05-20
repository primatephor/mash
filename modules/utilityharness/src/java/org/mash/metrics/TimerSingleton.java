package org.mash.metrics;

import org.apache.log4j.Logger;

import java.util.Timer;

/**
 * @author
 * @since Mar 18, 2011 12:47:31 PM
 */
public class TimerSingleton
{
    private static final Logger log = Logger.getLogger(TimerSingleton.class.getName());
    private static TimerSingleton ourInstance = new TimerSingleton();
    private static boolean shutdown = false;
    private Timer timer;

    public static TimerSingleton getInstance()
    {
        return ourInstance;
    }

    private TimerSingleton()
    {
        Runtime.getRuntime().addShutdownHook(new ShutdownRunner());
    }

    public Timer getTimer()
    {
        if (timer == null && !shutdown)
        {
            timer = new Timer();
        }
        return timer;
    }

    private static class ShutdownRunner extends Thread
    {
        @Override
        public void run()
        {
            log.info("Shutting down Metrics Timer");
            getInstance().getTimer().cancel();
            shutdown = true;
        }
    }
}
