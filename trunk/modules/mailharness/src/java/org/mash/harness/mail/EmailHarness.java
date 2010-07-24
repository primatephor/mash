package org.mash.harness.mail;

import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.RunResponse;

import java.util.List;

/**
 *
 * Configurations:
 * <ul>
 * <li>smtp_server (the email server url)</li>
 * <li>user (the db username)</li>
 * <li>password (the db password)</li>
 * </ul>
 * <p/>
 * Parameters:
 * 
 * @author teastlack
 * @since Jul 23, 2010 5:09:07 PM
 */
public class EmailHarness extends BaseEmailHarness implements RunHarness
{
    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        throw new UnsupportedOperationException("Method run not yet implemented");
    }

    public RunResponse getResponse()
    {
        throw new UnsupportedOperationException("Method getResponse not yet implemented");
    }
}
