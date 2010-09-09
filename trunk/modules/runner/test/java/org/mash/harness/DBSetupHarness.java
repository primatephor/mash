package org.mash.harness;

/**
 * User: teastlack Date: Jul 1, 2009 Time: 6:33:48 PM
 */
public class DBSetupHarness extends BaseHarness implements SetupHarness
{
    public Boolean setupCalled = false;

    public void setup()
    {
        setupCalled = true;
    }
}