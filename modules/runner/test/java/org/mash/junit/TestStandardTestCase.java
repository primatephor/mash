package org.mash.junit;

import junit.framework.TestCase;
import org.mash.config.ScriptDefinition;
import org.mash.config.Suite;
import org.mash.harness.DBSetupHarness;
import org.mash.harness.Harness;
import org.mash.harness.HttpRunHarness;
import org.mash.harness.HttpVerifyHarness;
import org.mash.loader.ScriptDefinitionLoader;
import org.mash.loader.SuiteLoader;
import org.mash.loader.harnesssetup.AnnotatedHarness;

import java.util.List;

/**
 *  Date: Jul 1, 2009 Time: 6:29:25 PM
 */
public class TestStandardTestCase extends TestCase
{
    public void testBaseRun() throws Throwable
    {
        System.setProperty("my.url", "MyURL");
        System.setProperty("script.runner", "org.mash.junit.MyScriptRunner");

        Suite suite = new SuiteLoader().loadSuite("org/mash/junit/suite.xml");
        List<ScriptDefinition> scripts = new ScriptDefinitionLoader().pullSubDefinitions((ScriptDefinition) suite.getScriptOrParallel().get(0), suite.getPath());
        StandardTestCase theCase = new StandardTestCase(scripts.get(0));
        theCase.runTest();
        List<Harness> harnesses = ((MyScriptRunner) theCase.getHarnessRunner()).getHarnesses();

        DBSetupHarness dbSetupHarness = (DBSetupHarness) ((AnnotatedHarness) harnesses.get(0)).getWrap();
        assertEquals(Boolean.TRUE, dbSetupHarness.setupCalled);
        assertEquals("clean", dbSetupHarness.getConfiguration().get(0).getName());
        assertEquals("loadfile", dbSetupHarness.getParameters().get(0).getName());
        assertEquals("<DataSet>\n" +
                     "    Here's the variable: sometext\n" +
                     "</DataSet>", dbSetupHarness.getParameters().get(0).getValue().trim());

        HttpRunHarness httpRunHarness = (HttpRunHarness) ((AnnotatedHarness) harnesses.get(1)).getWrap();
        assertEquals(Boolean.TRUE, httpRunHarness.runCalled);
        assertEquals("url", httpRunHarness.getConfigs().get(0).getName());

        HttpVerifyHarness httpVerifyHarness = (HttpVerifyHarness) ((AnnotatedHarness) harnesses.get(2)).getWrap();
        assertEquals(Boolean.TRUE, httpVerifyHarness.verifyCalled);
        assertEquals("status", httpVerifyHarness.getConfigs().get(0).getName());
        assertEquals("title", httpVerifyHarness.getConfigs().get(1).getName());
        assertEquals("My Page Title", httpVerifyHarness.getConfigs().get(1).getValue());

        httpRunHarness = (HttpRunHarness) ((AnnotatedHarness) harnesses.get(3)).getWrap();
        assertEquals(Boolean.TRUE, httpRunHarness.runCalled);
        assertEquals("url", httpRunHarness.getConfigs().get(0).getName());
        assertEquals("session", httpRunHarness.getParams().get(0).getName());
        assertEquals("login_session", httpRunHarness.getParams().get(0).getValue());
    }
}
