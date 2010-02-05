package org.mash.harness;

/**
 * Running tests can be done with a different runner by specifying 'system.test.runner' and implementing the
 * 'org.mash.harness.HarnessRunner' interface.  Default is the 'org.mash.harness.StandardHarnessRunner'.
 *
 * @author: teastlack
 * @since: Sep 19, 2009
 */
public class RunnerFactory
{
    private static RunnerFactory ourInstance = new RunnerFactory();
    public static String RUNNER = System.getProperty("system.test.runner", "org.mash.harness.StandardScriptRunner");

    public static RunnerFactory getInstance()
    {
        return ourInstance;
    }

    private RunnerFactory()
    {
    }

    public ScriptRunner buildRunner(String className) throws InstantiationException
    {
        ScriptRunner result;
        try
        {
            result = (ScriptRunner) Class.forName(RUNNER).newInstance();
        }
        catch (Exception e)
        {
            InstantiationException ex = new InstantiationException("Unable to create script runner " + className);
            ex.initCause(e);
            throw ex;
        }
        return result;
    }

    public ScriptRunner buildRunner() throws InstantiationException
    {
        return buildRunner(RUNNER);
    }
}
