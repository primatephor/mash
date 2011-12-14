package org.mash.harness;

import java.util.List;

/**
 * @author: teastlack
 * @since: Jun 13, 2011
 */
public interface Runner
{
    /**
     * For subscript invocation, need to retrieve the previous run information and supply to the
     * super script.
     *
     * @return runs
     */
    List<RunHarness> getPreviousRuns();

    /**
     * For subscript invocation, retrieve setup information from subscript.
     *
     * @return setups
     */
    List<SetupHarness> getSetupHarnesses();

    /**
     * and the last run from the subscript
     *
     * @return last run harness
     */
    RunHarness getLastRun();
}
