package org.mash.metrics;

import junit.framework.TestCase;

/**
 * @author
 * @since Feb 16, 2011 6:24:25 PM
 */
public class TestStatistics extends TestCase
{
    public void testSimple() throws InterruptedException
    {
        BaseMetrics stats = new BaseMetrics("test");
        assertEquals(null, stats.getMax());
        assertEquals(null, stats.getMin());
        assertEquals(null, stats.average());
        long total = stats.getTotal();
        assertTrue("Total too long", total <=10l);
        assertEquals(0, stats.getCount());

        stats.start();
        Thread.sleep(1000l);
        long diff = stats.end();
        assertTrue("Diff too long", diff <=1100l);

        stats.start();
        Thread.sleep(1000l);
        diff = stats.end();
        assertTrue("Diff too long", diff <=1100l);

        long totalDiff = stats.getTotal();
        assertTrue("Diff too long", totalDiff <=2100l);

        stats.start();
        Thread.sleep(3000l);
        diff = stats.end();
        assertTrue("Diff too long", diff <=3100l);

        assertEquals(3, stats.getCount());

        long max = stats.getMax();
        assertTrue("Diff too long", max <=3100l);

        long min = stats.getMin();
        assertTrue("Diff too long", min <=1100l);

        long average = stats.average();
        assertTrue("Diff too long", average <=1700l);
    }

    public void testAddStatistics() throws InterruptedException
    {
        BaseMetrics stats1 = new BaseMetrics("test1");
        BaseMetrics stats2 = new BaseMetrics("test2");
        BaseMetrics stats3 = new BaseMetrics("test1");

        stats1.start();
        Thread.sleep(1000l);
        long diff = stats1.end();
        assertTrue("Diff too long", diff <=1100l);

        stats2.start();
        Thread.sleep(1000l);
        diff = stats2.end();
        assertTrue("Diff too long", diff <=1100l);

        stats3.start();
        Thread.sleep(1500l);
        diff = stats3.end();
        assertTrue("Diff too long", diff <=1600l);

        //added
        stats1.add(stats3);

        long totalDiff = stats1.getTotal();
        assertTrue("Diff too long "+totalDiff, totalDiff <=2600l);
        assertTrue("Diff too short", totalDiff >=2500l);
        long max = stats1.getMax();
        assertTrue("Diff too long "+max, max <=1600l);
        long min = stats1.getMin();
        assertTrue("Diff too long "+min, min <=1100l);
        long average = stats1.average();
        assertTrue("Diff too long "+average, average <=1300l);
        assertTrue("Diff too short", average >= 1200l);
        assertEquals(2, stats1.getCount());
    }
}
