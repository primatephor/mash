package org.mash.metrics;

import java.util.Date;

/**
 * this interface is only meant to limit the usage of the Statistics class from the manager.  This allows the manager
 * to provide standard noop stats when it's turned off.
 *
 * @author teastlack
 * @since Feb 18, 2011 10:05:09 AM
 */
public interface Metrics
{
    String getEntity();
    void start();
    Long end();
    Long average();
    Long getTotal();
    Long getMax();
    int getCount();
    Long getMin();
    Date getStart();
    Long getEnd();
}
