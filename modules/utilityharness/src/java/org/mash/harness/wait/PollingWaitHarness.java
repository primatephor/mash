/*******************************************************************************
 * Copyright (c) 2010 Ensenda, Inc. All Rights Reserved.
 * This code  is the  sole  property  of  Ensenda Inc.,
 * and is protected  by  copyright  under the  laws of the United
 * States. This program is confidential, proprietary, and a trade
 * secret, not to be disclosed without written authorization from
 * Ensenda Inc.  Any  use, duplication, or  disclosure
 * of  this  program  by other than Ensenda Inc. and its
 * assigned licensees is strictly forbidden by law.
 ******************************************************************************/

package org.mash.harness.wait;

import org.apache.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;

import java.util.List;

/**
 *
 * @author teastlack
 * @since Sep 23, 2010 10:32:44 AM
 */
public abstract class PollingWaitHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(PollingWaitHarness.class.getName());

    //timeout after 1 minute by default
    private Integer timeoutMillis = 60 * 1000;
    //poll every 5 seconds by default
    private Integer pollMillis = 5 * 1000;

    @Override
    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        long current = timeoutMillis;
        long timeToWait = pollMillis;
        boolean isComplete = false;
        while (current > 0 && !isComplete)
        {
            isComplete = poll(previous, setups);
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
     * @param previous list of runs
     * @param setups list of setup steps
     * @return true if polling found what it was looking for
     */
    protected abstract boolean poll(List<RunHarness> previous, List<SetupHarness> setups);

    public void setTimeoutMillis(String timeoutMillis)
    {
        this.timeoutMillis = Integer.valueOf(timeoutMillis);
    }

    public void setPollMillis(String pollMillis)
    {
        this.pollMillis = Integer.valueOf(pollMillis);
    }

}
