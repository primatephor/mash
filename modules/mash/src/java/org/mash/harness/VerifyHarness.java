package org.mash.harness;

import java.util.List;

/**
 *  Date: Jul 1, 2009 Time: 9:21:08 AM
 */
public interface VerifyHarness extends Harness
{
    /**
     * Verify the specified run appropriately.  An implementation will retrieve the run response from the harness and
     * verify it.
     *
     * @param run   harness that invoked some action
     * @param setup list containing configurations.
     */
    void verify(RunHarness run, List<SetupHarness> setup);
}
