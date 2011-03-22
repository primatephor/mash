package org.mash.metrics;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Manage statistics.  Because we don't want these added forever and ever, if new stats are gathered and the last
 * check time is greater than the CULL_CHECK, go through all the currently gathered stats and remove them.
 * <p/>
 * Statistics are extended so calling .end() will put the totals into the stats map.
 *
 * @author teastlack
 * @since Feb 17, 2011 2:21:10 PM
 */
public class MetricsManager
{
    private static final Logger log = Logger.getLogger(MetricsManager.class.getName());
    private static MetricsManager ourInstance = new MetricsManager();

    private Map<String, Metrics> stats = new HashMap<String, Metrics>();
    private Map<String, Metrics> badStats = new HashMap<String, Metrics>();

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
        getInstance().stats = new HashMap<String, Metrics>();
        getInstance().badStats = new HashMap<String, Metrics>();
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
                if (getInstance().stats.size() > 0)
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
        return getInstance().stats;
    }

    public static Map<String, Metrics> getBadStats()
    {
        return getInstance().badStats;
    }

    private BaseMetrics buildStats(String entity)
    {
        return new TimedMetrics(entity, this);
    }

    private Lock lock = new ReentrantLock();

    protected void addStats(BaseMetrics statistics,
                            Map<String, BaseMetrics> stats)
    {
        BaseMetrics managed = stats.get(statistics.getEntity());
        if (managed == null)
        {
            lock.lock();
            try
            {
                //double check because may have occured outside of lock
                if (stats.get(statistics.getEntity()) == null)
                {
                    stats.put(statistics.getEntity(), statistics);
                }
                else
                {
                    managed = stats.get(statistics.getEntity());
                }
            }
            finally
            {
                lock.unlock();
            }
        }
        if (managed != null)
        {
            managed.add(statistics);
        }
    }

}
