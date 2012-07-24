package org.mash.harness.wait;

import junit.framework.TestCase;

import java.util.Date;

/**
 *
 * @author teastlack
 * @since Sep 28, 2009 12:24:42 PM
 *
 */
public class TestWaitHarness extends TestCase
{
    public void testSimpleWait()
    {
        TimedWaitRunHarness timedWait = new TimedWaitRunHarness();
        timedWait.setWaitTime("1000");
        Date start = new Date();
        timedWait.run(null);
        Date end = new Date();
        assertTrue("End is not 1000ms after start", (end.getTime() - start.getTime()) >= 1000);
        assertEquals("1000", timedWait.getResponse().getString());
    }
}
