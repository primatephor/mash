package org.mash.metrics;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Manage metrics gathering.
 *
 * Because we don't want these added forever and ever, a wrapping timer (TimedMetrics) stops any
 * metric after it has run for 10 minutes.
 *
 * Two groups of Metrics are maintained by the timer: regular and bad.  Anytime a Metric is ended
 * due to a timeout, it's added to the 'bad' group, to prevent the regular group from being weighted
 * with improper runs.
 *
 * @author teastlack
 * @since Feb 17, 2011 2:21:10 PM
 */
public class MetricsManager
{
    private static final Logger log = Logger.getLogger(MetricsManager.class.getName());
    private static MetricsManager ourInstance = new MetricsManager();

    private Map<String, Metrics> regularMetrics = new HashMap<String, Metrics>();
    private Map<String, Metrics> badMetrics = new HashMap<String, Metrics>();

    private NoOpMetrics noop = new NoOpMetrics();

    public static MetricsManager getInstance()
    {
        return ourInstance;
    }

    private MetricsManager()
    {
    }

    public static void reset()
    {
        log.info("Resetting Manager");
        getInstance().regularMetrics = new HashMap<String, Metrics>();
        getInstance().badMetrics = new HashMap<String, Metrics>();
    }

    public static Metrics startStats(Class base)
    {
        return startStats(base, null);
    }

    public static Metrics startStats(Class base,
                                               Method method)
    {
        StringBuilder builder = new StringBuilder();
        if (base != null)
        {
            builder.append(base.getName());
            if (method != null)
            {
                builder.append(".");
                builder.append(method.getName());
            }
        }
        return startStats(builder.toString());
    }

    public static Metrics startStats(String entity)
    {
        Metrics result = getInstance().noop;
        try
        {
            if (Configuration.isActive())
            {
                result = getInstance().buildStats(entity);
            }
            else
            {
                if (getInstance().regularMetrics.size() > 0)
                {
                    reset();
                }
            }
        }
        catch (Throwable t)
        {
            log.error("Problem starting metrics for " + entity, t);
        }
        return result;
    }

    public static Map<String, Metrics> getStats()
    {
        return getInstance().regularMetrics;
    }

    public static Map<String, Metrics> getBadStats()
    {
        return getInstance().badMetrics;
    }

    private BaseMetrics buildStats(String entity)
    {
        return new TimedMetrics(entity, this);
    }

    private Lock lock = new ReentrantLock();

    /**
     * Add the metric to the given map.  If one is present, then it's added to that metric.  If
     * there is no metric yet in that map, then it becomes the base metric and all others are added
     * to it.
     *
     * @param metrics to add
     * @param metricsMap to put it in
     */
    protected void add(Metrics metrics, Map<String, Metrics> metricsMap)
    {
        Metrics managed = metricsMap.get(metrics.getEntity());
        if (managed == null)
        {
            lock.lock();
            try
            {
                //double check because may have occured outside of lock
                if (metricsMap.get(metrics.getEntity()) == null)
                {
                    metricsMap.put(metrics.getEntity(), metrics);
                }
                else
                {
                    managed = metricsMap.get(metrics.getEntity());
                }
            }
            finally
            {
                lock.unlock();
            }
        }
        if (managed != null)
        {
            managed.add(metrics);
        }
    }

}
