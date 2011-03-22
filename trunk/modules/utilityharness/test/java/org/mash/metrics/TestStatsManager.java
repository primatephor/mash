package org.mash.metrics;

import junit.framework.TestCase;

/**
 * @author teastlack
 * @since Feb 17, 2011 2:38:43 PM
 */
public class TestStatsManager extends TestCase
{
    @Override
    protected void setUp() throws Exception
    {
        Configuration.active();
    }

    public void testManagement() throws InterruptedException
    {
        Metrics stats1 = MetricsManager.startStats("test1");
        Thread.sleep(1000l);
        long diff = stats1.end();
        assertTrue("Diff too long", diff <=1100l);

        Metrics stats2 = MetricsManager.startStats("test2");
        Thread.sleep(1000l);
        diff = stats2.end();
        assertTrue("Diff too long", diff <=1100l);

        Metrics stats3 = MetricsManager.startStats("test1");
        Thread.sleep(1500l);
        diff = stats3.end();
        assertTrue("Diff too long", diff <=1600l);

        stats1 = MetricsManager.getStats().get("test1");
        stats2 = MetricsManager.getStats().get("test2");

        long totalDiff = stats1.getTotal();
        assertTrue("Diff too long "+totalDiff, totalDiff <=2600l);
        assertTrue("Diff too short "+totalDiff, totalDiff >=2500l);
        long max = stats1.getMax();
        assertTrue("Diff too long", max <=1600l);
        long min = stats1.getMin();
        assertTrue("Diff too long", min <=1100l);
        long average = stats1.average();
        assertTrue("Diff too long", average <=1300l);
        assertTrue("Diff too short", average >= 1200l);
        assertEquals(2, stats1.getCount());

        assertEquals(1, stats2.getCount());
    }

    public void testCull() throws InterruptedException
    {
        MetricsManager.reset();
        TimedMetrics.CULL_TIME = 2000; // 2 seconds
        Metrics stats1 = MetricsManager.startStats("test1");
        Thread.sleep(3000l);
        MetricsManager.startStats("test2");
        Thread.sleep(2000l);
        long diff = stats1.getEnd();  //should have already ended
        assertTrue("Diff too long "+diff, diff <=2100l);
        assertTrue("Diff too short "+diff, diff >= 1900l);
    }

    public void testCullAdding() throws InterruptedException
    {
        MetricsManager.reset();
        TimedMetrics.CULL_TIME = 2000; // 2 seconds
        Metrics stats1 = MetricsManager.startStats("test1");
        Thread.sleep(3000l);
        stats1 = MetricsManager.startStats("test1");
        long diff = MetricsManager.getBadStats().get("test1").getTotal();
        assertEquals(1, MetricsManager.getBadStats().get("test1").getCount());
        assertTrue("Diff too long:"+diff, diff <=2100l);
        assertTrue("Diff too short "+diff, diff >= 1900l);

        Thread.sleep(3000l);
        diff = stats1.end();
        assertTrue("Diff too long:"+diff, diff <=2100l);
        assertTrue("Diff too short "+diff, diff >= 1900l);

        diff = stats1.getTotal();
        assertTrue("Diff too long:"+diff, diff <=2100l);
        assertTrue("Diff too short "+diff, diff >= 1900l);

        diff = MetricsManager.getBadStats().get("test1").getTotal();
        assertTrue("Diff too long:"+diff, diff <=4100l);
        assertTrue("Diff too short "+diff, diff >= 3900l);

        stats1 = MetricsManager.startStats("test1");
        Thread.sleep(1000l);
        diff = stats1.end();
        assertTrue("Diff too long:"+diff, diff <=1100l);
        assertTrue("Diff too short "+diff, diff >= 900l);

        //shoudl have made it correctly
        diff = MetricsManager.getStats().get("test1").getTotal();
        assertTrue("Diff too long:"+diff, diff <=1100l);
        assertTrue("Diff too short "+diff, diff >= 900l);

        //check that  this didnt change
        diff = MetricsManager.getBadStats().get("test1").getTotal();
        assertTrue("Diff too long:"+diff, diff <=4100l);
        assertTrue("Diff too short "+diff, diff >= 3900l);
        assertEquals(2, MetricsManager.getBadStats().get("test1").getCount());
        assertEquals(1, MetricsManager.getStats().get("test1").getCount());
    }

     public void testClassName() throws InterruptedException
    {
        Metrics stats1 = MetricsManager.startStats(this.getClass());
        Thread.sleep(1000l);
        long diff = stats1.end();
        assertTrue("Diff too long", diff <=1100l);
        assertEquals("com.ensenda.util.statistics.TestMetricsManager", stats1.getEntity());
    }
}
