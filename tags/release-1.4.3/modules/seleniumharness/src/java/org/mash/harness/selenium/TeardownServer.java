package org.mash.harness.selenium;

import org.mash.harness.BaseHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.TeardownHarness;
import org.mash.loader.HarnessName;

import java.util.List;

/**
 * Stop the selenium server
 *
 * @author teastlack
 * @since 12/13/11 4:55 PM
 */
@HarnessName(name = "selenium")
public class TeardownServer extends BaseHarness implements TeardownHarness
{
    @Override
    public void teardown(List<SetupHarness> setups)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
