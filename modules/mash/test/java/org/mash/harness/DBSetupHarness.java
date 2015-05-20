package org.mash.harness;

/**
 *  Date: Jul 1, 2009 Time: 6:33:48 PM
 */
public class DBSetupHarness extends BaseHarness implements SetupHarness
{
    public Boolean setupCalled = false;
    public static int callCount = 0;

    public DBSetupHarness()
    {
    }

    public void setup()
    {
        setupCalled = true;
        callCount++;
    }

    public static void reset()
    {
        callCount = 0;
    }
}
