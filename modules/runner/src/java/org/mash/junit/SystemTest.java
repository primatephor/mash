package org.mash.junit;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.mash.config.ScriptDefinition;
import org.mash.config.Suite;
import org.mash.loader.SuiteLoader;
import org.mash.main.SuiteRunner;

import java.util.List;

/**
 * Use Ant to invoke the system test.
 * <p/>
 * To specify the test suite file, set the system property "suite.file".
 * To specify the list of tags, supply the system proptery "suite.tags" as a comma separated list of tags.
 * <p/>
 * To change the marshaller (you can build your own, no need for xml) set the system property "suite.marshaller"
 * <p/>
 * @author
 * @since Jun 30, 2009 Time: 2:19:25 PM
 */
public class SystemTest extends SuiteRunner
{
    private static final Logger log = Logger.getLogger(SystemTest.class);
    protected static String SUITE = System.getProperty("suite.file", "SystemTest.xml");

    public SystemTest() throws Exception
    {
    }

    /**
     * Create the test suite by loading the system test file defined by "suite.file"
     *
     * @return test suite for junit to run
     * @throws Exception when any error happens
     */
    public static Test suite() throws Exception
    {
        SystemTest systemTest = new SystemTest();
        return systemTest.buildTests(SUITE);
    }

    public Test buildTests(String suiteFileName) throws Exception
    {
        log.info("Preparing to load system test(s) from context " + suiteFileName);
        Suite suite = new SuiteLoader().loadSuite(suiteFileName);

        TestSuite testSuite = new TestSuite();
        List<ScriptDefinition> scripts = buildScripts(suite, getTags());
        for (ScriptDefinition script : scripts)
        {
            testSuite.addTest(new StandardTestCase(script));
        }
        testSuite.setName(suite.getName());
        return testSuite;
    }
}
