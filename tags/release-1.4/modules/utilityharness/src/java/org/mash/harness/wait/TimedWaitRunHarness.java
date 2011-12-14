package org.mash.harness.wait;

import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessConfiguration;
import org.apache.log4j.Logger;
import org.mash.loader.HarnessName;

import java.util.List;

/**
 * Configurations:
 * <ul>
 * <li>wait_time: Time in milliseconds to sleep</li>
 * </ul>
 *
 * @author teastlack
 * @since Sep 28, 2009 11:34:45 AM
 */
@HarnessName(name = "wait")
public class TimedWaitRunHarness extends BaseHarness implements RunHarness
{
    private static final Logger LOG = Logger.getLogger(TimedWaitRunHarness.class.getName());
    private Long waitTime;
    private TimedResponse response;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        if (waitTime != null)
        {
            try
            {
                LOG.info("Waiting " + waitTime + " ms");
                Thread.sleep(waitTime);
            }
            catch (InterruptedException e)
            {
                getErrors().add(new HarnessError(this, "Error waiting for " + waitTime + " ms", e));
            }
        }
        else
        {
            getErrors().add(new HarnessError(this, "Configuration", "No wait time specified!"));
        }
    }

    public RunResponse getResponse()
    {
        if (response == null)
        {
            response = new TimedResponse(waitTime);
        }
        return response;
    }

    @HarnessConfiguration(name = "wait_time")
    public void setWaitTime(String waitTime)
    {
        this.waitTime = Long.valueOf(waitTime);
    }
}
