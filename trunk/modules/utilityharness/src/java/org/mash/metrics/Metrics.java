package org.mash.metrics;

import java.util.Date;

/**
 * this interface is only meant to limit the usage of the Metrics class from the manager.  This
 * allows the manager to provide standard noop stats when it's turned off, or wrap the metrics
 * gathering appropriately.
 *
 * Metrics can be run multiple times by calling 'start' and 'end'.  They can also be added to other
 * metrics to accumulate data.  For instance, each run of a method could get added up to show total
 * times.
 *
 * All times are CPU time.  If you have multiple cores running (say 4 cores) and the same method
 * runs on each for 1 second, then you'll have 4 total seconds.
 *
 * @author teastlack
 * @since Feb 18, 2011 10:05:09 AM
 */
public interface Metrics
{
    /**
     * This is the entity (usually class name and method) to gather stats on
     * @return name
     */
    String getEntity();

    /**
     * Start the metrics gathering
     */
    void start();

    /**
     * End the metrics gathering and return the length of time run
     * @return time (ms) of run
     */
    Long end();

    /**
     * Average time of the runs.  Calculated when runs are started/stopped multiple times, or when
     * other Metrics are added to this one.
     * @return average time run
     */
    Long average();

    /**
     * Total time for all runs of this metric (including added metrics)
     * @return total time run
     */
    Long getTotal();

    /**
     * @return Maximum time run for a Metric
     */
    Long getMax();

    /**
     * Total number of runs (start/end)
     * @return count of runs
     */
    int getCount();

    /**
     * @return Minimum time run for a Metric
     */
    Long getMin();

    /**
     * @return First start date
     */
    Date getStart();

    /**
     * @return End run time
     */
    Long getEnd();

    /**
     * Add the metric to this metric.  This adds the counts and totals up, as well as finding the
     * maximum and minimum of the two metrics.
     * @param metrics to be added
     */
    void add(Metrics metrics);
}
