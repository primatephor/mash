package org.mash.metrics;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * This contains the base level information shown in a logger's line.  Formatters use this data
 * to display as they see fit.
 *
 * @author teastlack
 * @since Feb 18, 2011 10:32:36 AM
 */
public class LoggerLine
{
    private Long totalCPU;
    private Metrics toLog;

    public LoggerLine(Long totalCPU, Metrics toLog)
    {
        this.totalCPU = totalCPU;
        this.toLog = toLog;
    }

    public String getEntity()
    {
        return toLog.getEntity();
    }

    public int getCount()
    {
        return toLog.getCount();
    }

    public Long getAverage()
    {
        return toLog.average();
    }

    public Long getTotal()
    {
        return toLog.getTotal();
    }

    public Long getMax()
    {
        return toLog.getMax();
    }

    public Long getMin()
    {
        return toLog.getMin();
    }

    public Date getStart()
    {
        return toLog.getStart();
    }

    public BigDecimal getPercent()
    {
        return getPercent(toLog);
    }

    private BigDecimal getPercent(Metrics numerator)
    {
        BigDecimal result = BigDecimal.ZERO;
        if (numerator != null && totalCPU != null && totalCPU > 0)
        {
            Long lNum = numerator.getTotal();
            if (lNum != null)
            {
                BigDecimal num = BigDecimal.valueOf(lNum);
                BigDecimal den = BigDecimal.valueOf(totalCPU);
                BigDecimal calc = num.divide(den, 2, RoundingMode.HALF_UP);
                result = calc.multiply(BigDecimal.valueOf(100));
            }
        }
        return result;
    }
}
