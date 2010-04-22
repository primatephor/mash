package org.mash.harness.ftp;

import org.apache.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;

import java.util.List;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 12:21:09 PM
 *
 */
public class BogusFTPRun extends BaseHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(BogusFTPRun.class.getName());
    private RunResponse cannedResponse;

    public BogusFTPRun(RunResponse cannedResponse)
    {
        this.cannedResponse = cannedResponse;
    }

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        log.info("Run called");
    }

    public RunResponse getResponse()
    {
        return cannedResponse;
    }
}
