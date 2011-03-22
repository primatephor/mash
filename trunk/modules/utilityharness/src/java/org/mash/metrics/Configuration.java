package org.mash.metrics;

import org.mash.metrics.formatter.PrettyFormatter;
import org.mash.metrics.formatter.BaseFormatter;


/**
 * @author teastlack
 * @since Feb 18, 2011 9:59:00 AM
 */
public class Configuration
{
    private static Configuration ourInstance = new Configuration();

    private State state = Configuration.State.INACTIVE;
    private String logName = "stats";
    private String format = "basic";
    private Boolean active;

    private static final String METRICS_RUNNING = "metrics.running";
    private static final String METRICS_FORMAT = "metrics.format";

    public static Configuration getInstance()
    {
        return ourInstance;
    }

    public Configuration()
    {
        ourInstance = this;
    }

    public static String getLogName()
    {
        return getInstance().logName;
    }

    public static Formatter getFormatter()
    {
        Formatter result;
        if ("pretty".equalsIgnoreCase(getFormat()))
        {
            result = new PrettyFormatter();
        }
        else
        {
            result = new BaseFormatter();
        }
        return result;
    }

    public static State active()
    {
        return getInstance().state = Configuration.State.ACTIVE;
    }

    public static boolean isActive()
    {
        return Configuration.State.ACTIVE.equals(getInstance().state) && runMetrics();
    }

    private static boolean runMetrics()
    {
        return Boolean.valueOf(System.getProperty(METRICS_RUNNING, "true"));
    }

    private static String getFormat()
    {
        return System.getProperty(METRICS_FORMAT, "basic");
    }

    public enum State
    {
        ACTIVE,
        INACTIVE
    }
}
