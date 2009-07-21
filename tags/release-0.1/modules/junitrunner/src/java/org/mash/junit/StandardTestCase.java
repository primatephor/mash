package org.mash.junit;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.mash.config.ScriptDefinition;
import org.mash.harness.HarnessError;
import org.mash.harness.ScriptRunner;

import java.util.List;

/**
 * The standard test case accepts a Test definition and instantiates all components upon construction (setup, run,
 * verify, teardown).  This allows each harness to instantiate any necessary timings, etc before runtime.
 * <p/>
 * Error Formatting can be changed by extending the 'org.testfw.junit.ErrorFormatter' class and setting the environment
 * variable 'system.test.formatter'.
 * <p/>
 * Running tests can be done with a different runner by specifying 'system.test.runner' and implementing the
 * 'org.testfw.harness.HarnessRunner' interface.  Default is the 'org.testfw.harness.StandardHarnessRunner'.
 * <p/>
 * User: teastlack Date: Jul 1, 2009 Time: 3:15:38 PM
 */
public class StandardTestCase extends TestCase
{
    private static final Logger log = Logger.getLogger(StandardTestCase.class.getName());
    private static String FORMATTER = System.getProperty("system.test.formatter", "org.mash.junit.ErrorFormatter");
    private static String RUNNER = System.getProperty("system.test.runner", "org.mash.harness.StandardScriptRunner");

    private ScriptDefinition scriptDefinition;
    private ScriptRunner scriptRunner;
    private ErrorFormatter formatter;

    public StandardTestCase(ScriptDefinition definition) throws Exception
    {
        this.scriptDefinition = definition;
        formatter = (ErrorFormatter) Class.forName(FORMATTER).newInstance();
        scriptRunner = (ScriptRunner) Class.forName(RUNNER).newInstance();
        this.setName(this.scriptDefinition.getName());
    }

    /**
     * Override the runTest method, which simply looks at the junit name and invokes that name as the method.  Since we
     * know the method to invoke, and it's cleaner to call it straight away, just invoke the method directly.
     *
     * @throws Throwable
     */
    protected void runTest() throws Throwable
    {
        log.info("Running Test '" + this.getName() + "'");
        List<HarnessError> errors = this.scriptRunner.run(this.scriptDefinition);
        if (errors.size() > 0)
        {
            throw new AssertionFailedError(formatter.format(errors));
        }
    }

    public ScriptDefinition getTestDefinition()
    {
        return scriptDefinition;
    }

    public ScriptRunner getHarnessRunner()
    {
        return scriptRunner;
    }

    public ErrorFormatter getFormatter()
    {
        return formatter;
    }
}
