package org.mash.harness;


import java.util.List;

/**
 * Tearing down a test after run should happen everytime a test is run, except when a test is run in parallel.  In that
 * case, after all tests are complete all of the teardowns are run.
 * <p/>
 *  Date: Jul 1, 2009 Time: 9:22:53 AM
 */
public interface TeardownHarness extends Harness
{
    /**
     * Perform the necessary tasks for cleaning up and tearing down the test after a run.
     *
     * @param setups used by the test
     */
    void teardown(List<SetupHarness> setups);
}
