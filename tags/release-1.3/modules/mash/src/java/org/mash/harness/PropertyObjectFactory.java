package org.mash.harness;

import org.mash.loader.SuiteMarshaller;

import java.util.List;

/**
 * Build objects from common property values.  This is mostly a convenience class, but should help clean up and
 * centralize some of these properties.
 *
 * This is NOT built for efficiency.  If that's what you'd like, don't use this.
 *
 * @author teastlack
 * @since Sep 19, 2009
 */
public class PropertyObjectFactory
{
    private static PropertyObjectFactory ourInstance = new PropertyObjectFactory();
    public static String HARNESS_RUNNER = System.getProperty("harness.runner", "org.mash.harness.StandardHarnessRunner");
    public static String RUNNER = System.getProperty("script.runner", "org.mash.harness.StandardScriptRunner");
    public static String FORMATTER = System.getProperty("suite.error.formatter", "org.mash.tool.ErrorFormatter");
    public static String SUITE_MARSHALLER = System.getProperty("suite.marshaller", "org.mash.loader.JAXBSuiteMarshaller");

    public static PropertyObjectFactory getInstance()
    {
        return ourInstance;
    }

    private PropertyObjectFactory()
    {
    }

    public Object buildObject(String className) throws InstantiationException
    {
        Object result;
        try
        {
            result = Class.forName(className).newInstance();
        }
        catch (Exception e)
        {
            InstantiationException ex = new InstantiationException("Unable to create object " + className);
            ex.initCause(e);
            throw ex;
        }
        return result;
    }

    public Object buildProperty(String propertyName) throws InstantiationException
    {
        return buildProperty(System.getProperty(propertyName), null);
    }

    public Object buildProperty(String propertyName, String defaultValue) throws InstantiationException
    {
        return buildObject(System.getProperty(propertyName, defaultValue));
    }

    //helper methods for dealing with existing known properties
    public HarnessRunner buildHarnessRunner(List<RunHarness> previousRuns,
                                            List<SetupHarness> setupHarnesses) throws InstantiationException
    {
        HarnessRunner harnessRunner = (HarnessRunner) buildObject(HARNESS_RUNNER);
        harnessRunner.getPreviousRuns().addAll(previousRuns);
        harnessRunner.getSetupHarnesses().addAll(setupHarnesses);
        return harnessRunner;
    }

    public ScriptRunner buildRunner() throws InstantiationException
    {
        return (ScriptRunner) buildObject(RUNNER);
    }

    public Object buildFormatter() throws InstantiationException
    {
        return buildObject(FORMATTER);
    }

    public SuiteMarshaller buildMarshaller() throws InstantiationException
    {
        return (SuiteMarshaller) buildObject(SUITE_MARSHALLER);
    }
}
