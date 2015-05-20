package org.mash.metrics;

import java.util.Date;

/**
 * @author
 * @since Feb 18, 2011 10:11:09 AM
 */
public class NoOpMetrics implements Metrics
{
    public String getEntity()
    {
        return null;
    }

    public void start()
    {
    }

    public Long end()
    {
        return null;
    }

    public Long average()
    {
        return null;
    }

    public Long getTotal()
    {
        return null;
    }

    public Long getMax()
    {
        return null;
    }

    public int getCount()
    {
        return 0;
    }

    public Long getMin()
    {
        return null;
    }

    public Date getStart()
    {
        return null;
    }

    public Long getEnd()
    {
        return null;
    }

    public void add(Metrics statistics)
    {
    }
}
