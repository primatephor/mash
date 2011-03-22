package org.mash.metrics;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Log the statistics to a log file
 *
 * @author teastlack
 * @since Feb 17, 2011 4:41:59 PM
 */
public class MetricsLogger
{
    private Logger log;

    private long period;
    private Timer timer;
    private boolean running = false;

    public MetricsLogger(long period)
    {
        this.period = period;
        this.log = Logger.getLogger(Configuration.getLogName());
        Configuration.active();
        start();
    }

    public void start()
    {
        if (timer == null)
        {
            timer = new Timer();
        }
        this.timer.scheduleAtFixedRate(new LogTask(), new Date(), period);
    }

    public LoggerLine logStats(Metrics statistics, Map<String, Metrics> stats)
    {
        return logStats(getTotalCPU(stats), statistics);
    }

    public LoggerLine logStats(Long totalCpu, Metrics statistics)
    {
        return new LoggerLine(totalCpu, statistics);
    }

    protected Long getTotalCPU(Map<String, Metrics> stats)
    {
        Long totalCPU = 0l;
        for (String entity : stats.keySet())
        {
            Metrics statistics = stats.get(entity);
            totalCPU += statistics.getTotal();
        }
        return totalCPU;
    }

    protected void logStats(Map<String, Metrics> stats)
    {
        Formatter formatter = Configuration.getFormatter();
        Long totalCPU = getTotalCPU(stats);
        for (String entity : stats.keySet())
        {
            Metrics statistics = stats.get(entity);
            LoggerLine line = logStats(totalCPU, statistics);
            formatter.addLine(line);
        }

        StringBuilder buffer = new StringBuilder();
        if (formatter.length() > 1)
        {
            buffer.append("\n");
            buffer.append(formatter.format());
            log.info(buffer.toString());
        }
    }

    private class LogTask extends TimerTask
    {
        @Override
        public void run()
        {
            try
            {
                if (!running)
                {
                    running = true;
                    if (MetricsManager.getStats().size() > 0)
                    {
                        log.info("Gathering Metrics");
                        logStats(MetricsManager.getStats());
                    }

                    if (MetricsManager.getBadStats().size() > 0)
                    {
                        log.info("Gathering Outliers and Bad Data");
                        logStats(MetricsManager.getBadStats());
                    }
                }
            }
            finally
            {
                running = false;
            }
        }
    }
}
