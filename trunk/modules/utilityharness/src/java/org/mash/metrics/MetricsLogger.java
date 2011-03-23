package org.mash.metrics;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Log the statistics to a log file.  You must specify a log file name, and can control this through a reloadable
 * properties file or environment variables.
 *
 * Variables for the property file or environment:
 * metrics.logger.name (the name of the log4j logger to retrieve with Logger.getLogger())
 * metrics.running (true/false.  can turn off logging of metrics via properties file this way)
 * metrics.format (basic/pretty.  Basic is comma separated for spreadsheets, pretty adds spaces for readability)
 *
 * These properties may be used in the constructor also, so there's no need to have them present
 * 
 * @author teastlack
 * @since Feb 17, 2011 4:41:59 PM
 */
public class MetricsLogger
{
    private Logger log;

    private Timer timer;
    private boolean running = false;

    public MetricsLogger(long period, String format, String logName)
    {
        Configuration.getInstance().setFormat(format);
        Configuration.getInstance().setLogName(logName);
        start(period);
    }

    public MetricsLogger(long period, String propertyFileName)
    {
        Configuration.getInstance().setPropertyFileName(propertyFileName);
        start(period);
    }

    public MetricsLogger(long period)
    {
        start(period);
    }

    /**
     * Restart the timer.  This cancels all timer jobs and creates a brand new timer
     *
     * @param period milliseconds between runs of logging
     */
    public void start(long period)
    {
        this.log = Logger.getLogger(Configuration.getLogName());
        Configuration.active();
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
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
        if (formatter.length() > 0)
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
                    if (MetricsManager.getRegular().size() > 0)
                    {
                        log.info("Gathering Metrics");
                        logStats(MetricsManager.getRegular());
                    }

                    if (MetricsManager.getBad().size() > 0)
                    {
                        log.info("Gathering Outliers and Bad Data");
                        logStats(MetricsManager.getBad());
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
