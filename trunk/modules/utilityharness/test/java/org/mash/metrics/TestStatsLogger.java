package org.mash.metrics;

import junit.framework.TestCase;

import java.math.BigDecimal;

import org.mash.metrics.formatter.BaseFormatter;
import org.mash.metrics.formatter.PrettyFormatter;

/**
 * @author teastlack
 * @since Feb 17, 2011 4:55:50 PM
 */
public class TestStatsLogger extends TestCase
{
    public void testLogging() throws InterruptedException
    {
        MetricsLogger logger = new MetricsLogger(1000l);
        MetricsManager.reset();
        
        Metrics stats1 = MetricsManager.startStats("testLogging1");
        Thread.sleep(1000l);
        long diff = stats1.end();
        assertTrue("Diff too long", diff <=1100l);

        Metrics stats2 = MetricsManager.startStats("testLogging2");
        Thread.sleep(1000l);
        diff = stats2.end();
        assertTrue("Diff too long", diff <=1100l);

        Metrics stats3 = MetricsManager.startStats("testLogging1");
        Thread.sleep(2000l);
        diff = stats3.end();
        assertTrue("Diff too long", diff <=3100l);
        Thread.sleep(2000l);


        LoggerLine logline = logger.logStats(MetricsManager.getStats().get("testLogging1"), MetricsManager.getStats());
        assertEquals("testLogging1", logline.getEntity());
        assertEquals(2, logline.getCount());

        long average =  logline.getAverage();
        assertTrue("too large", average <=1600l);
        assertTrue("too small", average >=1400l);

        long total =  logline.getTotal();
        assertTrue("too large", total <=3100l);
        assertTrue("too small", total >=2900l);

        BigDecimal percent =  logline.getPercent(); //75%
        assertEquals(75.0, percent.doubleValue());

        long max =  logline.getMax();
        assertTrue("too large", max <=2100l);
        assertTrue("too small", max >=1900l);

        long min =  logline.getMin();
        assertTrue("too large", min <=1100l);
        assertTrue("too small", min >=900l);

        BaseFormatter formatter = new BaseFormatter();
        String lineString = formatter.formatData(logger.logStats(MetricsManager.getStats().get("testLogging2"), MetricsManager.getStats()));
        assertContains("testLogging2", lineString);
        assertContains("1,0m 1.0s,0m 1.0s,25.00,0m 1.0s,0m 1.0s", lineString);

        //check pretty formatting
        formatter = new PrettyFormatter();
        formatter.addLine(logger.logStats(MetricsManager.getStats().get("testLogging2"), MetricsManager.getStats()));
        String colString = formatter.formatColumnNames();
        lineString = formatter.formatData();
        //HAVE TO IGNORE START TIMES
        assertContains("Entity      ,", colString);
        assertContains("testLogging2,", lineString);
        assertContains(", # of Calls, Average, Total  , % of CPU, Max Time, Min Time", colString);
        assertContains(", 1         , 0m 1.0s, 0m 1.0s, 25.00   , 0m 1.0s , 0m 1.0s", lineString);

        logline = logger.logStats(MetricsManager.getStats().get("testLogging2"), MetricsManager.getStats());
        percent =  logline.getPercent(); //25%
        assertEquals(25.0, percent.doubleValue());
    }

    public void testManyAdds() throws InterruptedException
    {
        MetricsLogger logger = new MetricsLogger(100000l);
        MetricsManager.reset();

        Metrics stats1 = MetricsManager.startStats("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.startStats("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.startStats("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.startStats("testManyAdds1");
        Metrics stats2 = MetricsManager.startStats("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.startStats("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.startStats("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.startStats("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.startStats("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.startStats("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats2.end();
        Thread.sleep(2000l);


        LoggerLine logline = logger.logStats(MetricsManager.getStats().get("testManyAdds1"), MetricsManager.getStats());
        assertEquals("testManyAdds1", logline.getEntity());
        assertEquals(10, logline.getCount());

        long average =  logline.getAverage();
        assertTrue("too large", average <=1600l);
        assertTrue("too small", average >=1400l);

        long total =  logline.getTotal();
        assertTrue("too large "+total, total <=15100l);
        assertTrue("too small", total >=1400l);

        BigDecimal percent =  logline.getPercent(); //50%
        assertTrue("not equal "+percent, percent.doubleValue() == 100);

        long max =  logline.getMax();
        assertTrue("too large "+max, max <=6100l);
        assertTrue("too small", max >=5900l);

        long min =  logline.getMin();
        assertTrue("too large "+min, min <=1100l);
        assertTrue("too small", min >=900l);

    }

    public void assertContains(String toCheck, String value)
    {
        if(!value.contains(toCheck))
        {
            throw new AssertionError(value+" does not contain '"+toCheck+"'");
        }
    }
}
