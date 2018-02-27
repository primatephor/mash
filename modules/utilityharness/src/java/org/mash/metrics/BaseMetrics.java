package org.mash.metrics;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author
 * @since Feb 16, 2011 6:20:49 PM
 */
public class BaseMetrics implements Metrics
{
    private static final Logger log = LogManager.getLogger(BaseMetrics.class.getName());
    private String entity;
    private Date start;
    private Long total;
    private Long max;
    private int count = 0;
    private Long min;
    private Long end;

    public BaseMetrics(String entity)
    {
        this.entity = entity;
        start();
    }

    public void reset()
    {
        this.start = null;
        this.total = null;
        this.max = null;
        this.min = null;
        this.end = null;
        this.count = 0;
    }

    public void start()
    {
        this.start = new Date();
    }

    public Long end()
    {
        try
        {
            Date endDate = new Date();
            end = endDate.getTime() - this.start.getTime();
            this.total = addTotal(end);
            this.max = max(end, this.max);
            this.min = min(end, this.min);
            this.count++;
        }
        catch (Throwable t)
        {
            log.error("Problem ending stats gathering", t);
        }
        return end;
    }

    private Long addTotal(Long toAdd)
    {
        Long result = toAdd;
        if (this.total != null && toAdd != null)
        {
            result = this.total + toAdd;
        }
        return result;
    }

    private Long min(Long first,
                     Long second)
    {
        Long result = first;
        if (second != null && result != null && result > second)
        {
            result = second;
        }
        return result;
    }

    private Long max(Long first,
                     Long second)
    {
        Long result = first;
        if (second != null && result != null && second > result)
        {
            result = second;
        }
        return result;
    }

    public Long average()
    {
        Long result = null;
        if (this.count > 0)
        {
            result = this.total / this.count;
        }
        return result;
    }

    public String getEntity()
    {
        return entity;
    }

    public Long getTotal()
    {
        Long result = total;
        if (result == null)
        {
            if (start != null)
            {
                result = new Date().getTime() - start.getTime();
            }
            else
            {
                result = 0l;
            }
        }
        return result;
    }

    public Long getMax()
    {
        return max;
    }

    public int getCount()
    {
        return count;
    }

    public Long getMin()
    {
        return min;
    }

    public Date getStart()
    {
        return start;
    }

    public Long getEnd()
    {
        return end;
    }

    private Lock lock = new ReentrantLock();

    public void add(Metrics metrics)
    {
        lock.lock();
        try
        {
            this.total = addTotal(metrics.getTotal());
            this.max = max(this.max, metrics.getMax());
            this.min = min(this.min, metrics.getMin());
            this.count += metrics.getCount();
        }
        finally
        {
            lock.unlock();
        }
    }
}
