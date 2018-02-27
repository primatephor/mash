package org.mash.metrics;

import org.apache.logging.log4j.Logger;
import junit.framework.TestCase;
import org.mash.metrics.formatter.BaseFormatter;
import org.mash.metrics.formatter.PrettyFormatter;

import java.math.BigDecimal;

/**
 * @author
 * @since Feb 17, 2011 4:55:50 PM
 */
public class TestStatsLogger extends TestCase
{
    private MetricsLogger logger;

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
        Configuration.getInstance().setSnapshot(Boolean.FALSE);
        if(logger != null)
        {
            logger.stop();
        }
    }

    public void testLogging() throws InterruptedException
    {
        logger = new MetricsLogger(1000l, "basic", "stats");
        MetricsManager.reset();
        
        Metrics stats1 = MetricsManager.start("testLogging1");
        Thread.sleep(1000l);
        long diff = stats1.end();
        assertTrue("Diff too long", diff <=1100l);

        Metrics stats2 = MetricsManager.start("testLogging2");
        Thread.sleep(1000l);
        diff = stats2.end();
        assertTrue("Diff too long", diff <=1100l);

        Metrics stats3 = MetricsManager.start("testLogging1");
        Thread.sleep(2000l);
        diff = stats3.end();
        assertTrue("Diff too long", diff <=3100l);
        Thread.sleep(2000l);


        LoggerLine logline = logger.logStats(MetricsManager.getRegular().get("testLogging1"), MetricsManager.getRegular());
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
        String lineString = formatter.formatData(logger.logStats(MetricsManager.getRegular().get("testLogging2"), MetricsManager.getRegular()));
        assertContains("testLogging2", lineString);
        //assertContains("1,0m 1.000s,0m 1.000s,25.00,0m 1.000s,0m 1.000s", lineString);
        compareTimes("0m 1.000s,0m 1.000s,25.00,0m 1.000s,0m 1.000s", lineString);

        //check pretty formatting
        formatter = new PrettyFormatter();
        formatter.addLine(logger.logStats(MetricsManager.getRegular().get("testLogging2"), MetricsManager.getRegular()));
        String colString = formatter.formatColumnNames();
        lineString = formatter.formatData();
        //HAVE TO IGNORE START TIMES
        assertContains("Entity      ,", colString);
        assertContains("testLogging2,", lineString);
        assertContains(", # of Calls, Average  , Total    , % of CPU, Max Time , Min Time", colString);
        compareTimes("0m 1.000s, 0m 1.000s, 25.00   , 0m 1.000s, 0m 1.000s", lineString);

        logline = logger.logStats(MetricsManager.getRegular().get("testLogging2"), MetricsManager.getRegular());
        percent =  logline.getPercent(); //25%
        assertEquals(25.0, percent.doubleValue());
    }

    public void testManyAdds() throws InterruptedException
    {
        logger = new MetricsLogger(5000l, "pretty", "stats");
        MetricsManager.reset();

        Metrics stats1 = MetricsManager.start("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.start("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.start("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.start("testManyAdds1");
        Metrics stats2 = MetricsManager.start("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.start("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.start("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.start("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.start("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats1 = MetricsManager.start("testManyAdds1");
        Thread.sleep(1000l);
        stats1.end();
        stats2.end();
        Thread.sleep(2000l);


        LoggerLine logline = logger.logStats(MetricsManager.getRegular().get("testManyAdds1"), MetricsManager.getRegular());
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

    public void testLogSnapshot() throws InterruptedException
    {
        Configuration.getInstance().setSnapshot(Boolean.TRUE);
        MetricsManager.reset();

        logger = new MyMetLogger(2000l, "basic", "stats");

        Metrics stats1 = MetricsManager.start("testSnapLogging1");
        Thread.sleep(1000l);
        stats1.end();

        Metrics stats2 = MetricsManager.start("testSnapLogging2");
        Thread.sleep(1000l);
        stats2.end();

        Metrics stats3 = MetricsManager.start("testSnapLogging1");
        Thread.sleep(2000l);
        stats3.end();

        Thread.sleep(2000l);

        LoggerTester myLogger = (LoggerTester) logger.getLogger();
        for (Object o : myLogger.getLoggedItems())
        {
            System.out.println(o);
        }
        int size = myLogger.getLoggedItems().size();

        //notice that calculations are based on single entry
        //need to break this down better to test, COMMENT IF NECESSARY
        compareTimes("0m 2.000s,0m 2.000s,100.00,0m 2.000s,0m 2.000s", (String) myLogger.getLoggedItems().get(size - 1));
        assertEquals("Gathering Snapshot Metrics", myLogger.getLoggedItems().get(size - 3));
    }

    private class MyMetLogger extends MetricsLogger
    {
        private LoggerTester logger = new LoggerTester();

        public MyMetLogger(long period, String format, String logName)
        {
            super(period, format, logName);
        }

        @Override
        public Logger getLogger()
        {
            return logger;
        }
    }


    private void compareTimes(String expected, String actual)
    {
        if(actual.startsWith("\nEntity"))
        {
            actual = actual.split("\n")[2];
        }
        System.out.println("Comparing expected "+expected+" with "+actual);
        //break out expected times
        String[] expectedTimes = expected.split(",");
        //get actual values first
        String[] actuals = actual.split(",");
        compareTime(expectedTimes[0], actuals[3]);
        compareTime(expectedTimes[1], actuals[4]);
        assertEquals(expectedTimes[2].trim(), actuals[5].trim());
        compareTime(expectedTimes[3], actuals[6]);
        compareTime(expectedTimes[4], actuals[7]);
    }

    private void compareTime(String expectedTime, String actual)
    {
        expectedTime =expectedTime.trim();
        actual = actual.trim();
        System.out.println("Comparing expected "+expectedTime+" with "+actual);
        boolean found = false;
        //need to look for time within 10ms (because of slowness running tests
        for (int i = 0; i < 11; i++)
        {
            String testValue = actual.substring(0, actual.length()-2);
            testValue  = testValue+i+"s";
            if(expectedTime.equals(testValue))
            {
                found = true;
                break;
            }
        }
        if(!found)
        {
            throw new AssertionError("expected "+expectedTime+" but got "+actual);
        }
    }

    public void assertContains(String toCheck, String value)
    {
        if(!value.contains(toCheck))
        {
            throw new AssertionError(value+" does not contain '"+toCheck+"'");
        }
    }
}
