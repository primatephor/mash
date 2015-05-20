package org.mash.junit;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.mash.config.ScriptDefinition;
import org.mash.harness.HarnessContext;
import org.mash.harness.HarnessError;
import org.mash.harness.PropertyObjectFactory;
import org.mash.harness.ScriptRunner;
import org.mash.tool.ErrorFormatter;
import org.mash.tool.ErrorHandler;

import java.util.List;

/**
 * The standard test case accepts a Test definition and instantiates all components upon construction (setup, run,
 * verify, teardown).  This allows each harness to instantiate any necessary timings, etc before runtime.
 * <p/>
 * Error Formatting can be changed by extending the 'org.mash.junit.ErrorFormatter' class and setting the environment
 * variable 'suite.error.formatter'.
 * <p/>
 * Running tests can be done with a different runner by specifying 'script.runner' and implementing the
 * 'org.mash.harness.ScriptRunner' interface.  Default is the 'org.mash.harness.StandardScriptRunner'.
 *
 * @see org.mash.harness.PropertyObjectFactory
 *      <p/>
 *
 * @author
 * @since Jul 1, 2009 Time: 3:15:38 PM
 */
public class StandardTestCase extends TestCase
{
    private static final Logger log = Logger.getLogger(StandardTestCase.class.getName());

    private ScriptDefinition scriptDefinition;
    private ScriptRunner scriptRunner;
    private ErrorHandler handler;

    public StandardTestCase(ScriptDefinition definition) throws Exception
    {
        this.scriptDefinition = definition;
        this.scriptRunner = PropertyObjectFactory.getInstance().buildRunner();
        this.handler = new AssertionErrorHandler((ErrorFormatter) PropertyObjectFactory.getInstance().buildFormatter());
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
        log.info("\n********************************************************************************************\n" +
                 "Running Test '" + this.getName() + "'");
        List<HarnessError> errors = this.scriptRunner.run(this.scriptDefinition, new HarnessContext());
        handler.handleErrors(errors, this.scriptDefinition);
    }

    public ScriptDefinition getTestDefinition()
    {
        return scriptDefinition;
    }

    public ScriptRunner getHarnessRunner()
    {
        return scriptRunner;
    }

}
