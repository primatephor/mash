package org.mash.junit;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.mash.config.Suite;
import org.mash.loader.SuiteLoader;

import java.util.List;

/**
 * Use Ant to invoke the system test.
 * <p/>
 * To specify the test suite file, set the system property "system.test.file".
 * <p/>
 * To change the marshaller (you can build your own, no need for xml) set the system property "suite.marshaller"
 * <p/>
 * User: teastlack Date: Jun 30, 2009 Time: 2:19:25 PM
 */
public class SystemTest
{
    private static final Logger logger = Logger.getLogger(SystemTest.class);
    private static String SYSTEM_TEST = System.getProperty("system.test.file", "SystemTest.xml");

    /**
     * Create the test suite by loading the system test file defined by "system.test.file"
     *
     * @return test suite for junit to run
     * @throws Exception when any error happens
     */
    public static Test suite() throws Exception
    {
        logger.debug("Preparing to load system test(s) from context " + SYSTEM_TEST);

        Suite suite = new SuiteLoader().loadSuite(SYSTEM_TEST);
        TestSuite testSuite = new TestSuite();
        JunitTestBuilder builder = new JunitTestBuilder();
        List<Test> tests = builder.buildTests(suite);
        for (Test test : tests)
        {
            testSuite.addTest(test);
        }
        testSuite.setName(suite.getName());
        return testSuite;
    }


}
