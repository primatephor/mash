package org.mash.harness.ftp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessContext;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;

/**
 *
 * @author
 * @since Sep 22, 2009 12:21:09 PM
 *
 */
public class BogusFTPRun extends BaseHarness implements RunHarness
{
    private static final Logger log = LogManager.getLogger(BogusFTPRun.class.getName());

    private RunResponse cannedResponse;

    public BogusFTPRun(RunResponse cannedResponse)
    {
        this.cannedResponse = cannedResponse;
    }

    public void run(HarnessContext context)
    {
        log.info("Run called");
    }

    public RunResponse getResponse()
    {
        return cannedResponse;
    }
}
