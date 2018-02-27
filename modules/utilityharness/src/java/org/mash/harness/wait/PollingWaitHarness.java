package org.mash.harness.wait;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessContext;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;

/**
 *
 * @since Sep 23, 2010 10:32:44 AM
 */
public abstract class PollingWaitHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = LogManager.getLogger(PollingWaitHarness.class.getName());
    //timeout after 1 minute by default
    private Integer timeoutMillis = 60 * 1000;
    //poll every 1 seconds by default
    private Integer pollMillis = 1000;

    public void run(HarnessContext context)
    {
        long current = timeoutMillis;
        long timeToWait = pollMillis;
        boolean isComplete = false;
        while (current > 0 && !isComplete)
        {
            isComplete = poll(context);
            if (!isComplete)
            {
                try
                {
                    log.info("didn't find what I wanted, waiting:" + timeToWait+", remaining:"+current);
                    Thread.sleep(timeToWait);
                }
                catch (InterruptedException e)
                {
                    this.getErrors().add(new HarnessError(this, "Problem waiting for next polling", e));
                }
            }
            current -= timeToWait;
            //don't wait longer than what's remaining
            if(timeToWait > current)
            {
                timeToWait = current;
            }
        }

        if (!isComplete)
        {
            this.getErrors().add(buildPollingFailureError());
        }
    }

    protected HarnessError buildPollingFailureError()
    {
        return new HarnessError(this, "Polling Wait", "Timed out before polling succeeded");
    }

    /**
     * Poll the specified resource and return a boolean value indicating success.
     *
     * @return true if polling found what it was looking for
     * @param context of test run
     */
    protected abstract boolean poll(HarnessContext context);

    public void setTimeoutMillis(String timeoutMillis)
    {
        this.timeoutMillis = Integer.valueOf(timeoutMillis);
    }

    public void setPollMillis(String pollMillis)
    {
        this.pollMillis = Integer.valueOf(pollMillis);
    }

}
