package org.mash.junit;

import junit.framework.TestCase;
import org.mash.config.Suite;
import org.mash.harness.DBSetupHarness;
import org.mash.harness.Harness;
import org.mash.harness.HttpRunHarness;
import org.mash.harness.HttpVerifyHarness;
import org.mash.loader.ScriptLoaderProxy;
import org.mash.loader.SuiteLoader;

import java.util.List;

/**
 * User: teastlack Date: Jul 1, 2009 Time: 6:29:25 PM
 */
public class TestStandardTestCase extends TestCase
{
    public void testBaseRun() throws Throwable
    {
        System.setProperty("mercury.url", "MyURL");
        System.setProperty("system.test.runner", "org.mash.junit.MyScriptRunner");

        Suite suite = new SuiteLoader().loadSuite("org/mash/junit/suite.xml");
        StandardTestCase theCase = new StandardTestCase(new ScriptLoaderProxy("baseRun.xml", suite));
        theCase.runTest();
        List<Harness> harnesses = ((MyScriptRunner) theCase.getHarnessRunner()).getHarnesses();

        DBSetupHarness dbSetupHarness = (DBSetupHarness) harnesses.get(0);
        assertEquals(Boolean.TRUE, dbSetupHarness.setupCalled);
        assertEquals("clean", dbSetupHarness.getConfiguration().get(0).getName());
        assertEquals("loadfile", dbSetupHarness.getParameters().get(0).getName());
        assertEquals("<DataSet>\n" +
                     "    Here's the variable: sometext\n" +
                     "</DataSet>", dbSetupHarness.getParameters().get(0).getValue().trim());

        HttpRunHarness httpRunHarness = (HttpRunHarness) harnesses.get(1);
        assertEquals(Boolean.TRUE, httpRunHarness.runCalled);
        assertEquals("url", httpRunHarness.getConfigs().get(0).getName());

        HttpVerifyHarness httpVerifyHarness = (HttpVerifyHarness) harnesses.get(2);
        assertEquals(Boolean.TRUE, httpVerifyHarness.verifyCalled);
        assertEquals("status", httpVerifyHarness.getConfigs().get(0).getName());

        httpRunHarness = (HttpRunHarness) harnesses.get(3);
        assertEquals(Boolean.TRUE, httpRunHarness.runCalled);
        assertEquals("url", httpRunHarness.getConfigs().get(0).getName());
        assertEquals("session", httpRunHarness.getParams().get(0).getName());
        assertEquals("login_session", httpRunHarness.getParams().get(0).getValue());
    }
}
