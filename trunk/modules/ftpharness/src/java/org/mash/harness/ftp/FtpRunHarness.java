package org.mash.harness.ftp;

import org.mash.harness.BaseHarness;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;

import java.util.List;

/**
 *
 * @author teastlack
 * @since Sep 17, 2009 10:20:40 AM
 *
 */
public class FtpRunHarness extends BaseHarness implements RunHarness
{
    private String url;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        throw new UnsupportedOperationException("Method run not yet implemented");
    }

    public RunResponse getResponse()
    {
        throw new UnsupportedOperationException("Method getResponse not yet implemented");
    }
}
