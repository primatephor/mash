package org.mash.harness;

import org.mash.config.ScriptDefinition;

import java.util.List;

/**
 * An implementation of the HarnessRunner will examine the test definition and invoke the underlying harnesses
 * appropriately.  A more likely usage will be to extends the StandardHarnessRunner and modify the invocation of either
 * the setup, run, verify, or teardown harness invocation.
 * <p/>
 * User: teastlack Date: Jul 7, 2009 Time: 3:51:16 PM
 */
public interface ScriptRunner
{
    /**
     * Run the definition and return any errors
     * @param definition to run
     * @return errors generated
     * @throws Exception when something odd happens
     */
    List<HarnessError> run(ScriptDefinition definition) throws Exception;

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
