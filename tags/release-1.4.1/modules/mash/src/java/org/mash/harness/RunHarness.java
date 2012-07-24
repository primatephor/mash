package org.mash.harness;

/**
 * Running a test happens here.  After setup harnesses are run, one or more run harnesses are invoked.  A run harness
 * could be run after some verify harnesses have been run.
 * <p/>
 * User: teastlack Date: Jul 1, 2009 Time: 9:19:24 AM
 */
public interface RunHarness extends Harness
{
    /**
     * The framework invokes the test by calling the run method.  Each implementation has access to the previous runs
     * and the list of setup harness information is available to the entire test instance.
     * <p/>
     * The previous runs are helpful for chained runs, for example when a web login is invoked likely some
     * authentication information is a result.  The next run would retrieve and save that information for use in
     * navigation throughout a website.
     *
     * @param context harness contextual information
     */
    void run(HarnessContext context);

    /**
     * After a run, there should be some response available to other harnesses for examination.
     *
     * @return response of the run harness invocation
     */
    RunResponse getResponse();
}
