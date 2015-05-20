package org.mash.metrics;

import org.apache.log4j.Logger;
import org.mash.file.PropertiesFile;
import org.mash.metrics.formatter.BaseFormatter;
import org.mash.metrics.formatter.PrettyFormatter;


/**
 * Encapsulate the configuration of the logger and metrics gathering.  This will search for a properties file containing
 * the metrics names, or look for system environment variables.
 *
 * @author
 * @since Feb 18, 2011 9:59:00 AM
 */
public class Configuration
{
    private static final Logger log = Logger.getLogger(Configuration.class.getName());
    private static Configuration ourInstance = new Configuration();

    private State state = Configuration.State.INACTIVE;
    private String propertyFileName;

    private PropertiesFile properties;

    public static final String METRICS_LOGGER_NAME = "metrics.logger.name";
    public static final String METRICS_RUNNING = "metrics.running";
    public static final String METRICS_FORMAT = "metrics.format";
    public static final String METRICS_SNAPSHOT = "metrics.snapshot";

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
        return getInstance().getProperty(METRICS_LOGGER_NAME, "stats");
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
        log.info("Starting metrics");
        log.info("Logger:"+getLogName());
        log.info("Format:"+getFormat());
        log.info("Snapshot mode:"+getFormat());
        return getInstance().state = Configuration.State.ACTIVE;
    }

    public static boolean isActive()
    {
        return Configuration.State.ACTIVE.equals(getInstance().state) && runMetrics();
    }

    public static boolean runMetrics()
    {
        return Boolean.valueOf(getInstance().getProperty(METRICS_RUNNING, "true"));
    }

    public static String getFormat()
    {
        return getInstance().getProperty(METRICS_FORMAT, "basic");
    }

    public static Boolean isSnapshot()
    {
        return Boolean.valueOf(getInstance().getProperty(METRICS_SNAPSHOT, "false"));
    }

    public void setLogName(String logName)
    {
        System.getProperties().put(METRICS_LOGGER_NAME, logName);
    }

    public void setFormat(String format)
    {
        System.getProperties().put(METRICS_FORMAT, format);
    }

    public void setSnapshot(Boolean snapshot)
    {
        System.getProperties().put(METRICS_SNAPSHOT, snapshot.toString());
    }

    public void setPropertyFileName(String propertyFileName)
    {
        this.propertyFileName = propertyFileName;
    }

    protected String getProperty(String name, String defaultValue)
    {
        String result = null;
        if (properties == null && propertyFileName != null)
        {
            properties = new PropertiesFile(propertyFileName);
        }
        if (properties != null)
        {
            result = properties.getProperty(name);
        }
        if (result == null)
        {
            result = System.getProperty(name);
        }
        if (result == null)
        {
            result = defaultValue;
        }
        return result;
    }

    public enum State
    {
        ACTIVE,
        INACTIVE
    }
}
