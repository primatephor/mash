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
        WaitRunHarness wait = new WaitRunHarness();
        wait.setWaitTime("1000");
        Date start = new Date();
        wait.run(null, null);
        Date end = new Date();
        assertTrue("End is not 1000ms after start", (end.getTime() - start.getTime()) >= 1000);
        assertEquals("1000", wait.getResponse().getString());
    }
}
