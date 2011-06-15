package org.mash.harness;

import junit.framework.TestCase;
import org.mash.config.Script;
import org.mash.config.ScriptDefinition;
import org.mash.config.Suite;
import org.mash.loader.ScriptDefinitionLoader;
import org.mash.loader.ScriptLoaderProxy;
import org.mash.loader.SuiteLoader;
import org.mash.loader.harnesssetup.AnnotatedHarness;

/**
 * @author: teastlack
 * @since: Sep 19, 2009
 */
public class TestStandardScriptRunner extends TestCase
{
    public void testHarnessAndScript() throws Exception
    {
        System.setProperty("jdbc.url", "hey!");
        ScriptDefinitionLoader loader = new ScriptDefinitionLoader();
        SuiteLoader suiteLoader = new SuiteLoader();
        Suite suite = suiteLoader.loadSuite("org/mash/harness/testHarnessAndScript_data/suite1.xml");
        Script theScript = new Script();
        theScript.setFile("script1.xml");
        ScriptDefinition definition = loader.pullDefinition(theScript, suite);

        DBSetupHarness.reset();
        HttpRunHarness.reset();
        ScriptRunner runner = PropertyObjectFactory.getInstance().buildRunner();
        runner.run(definition);

        StandardScriptRunner standardScriptRunner = (StandardScriptRunner) runner;
        assertEquals(3, standardScriptRunner.getHarnesses().size());
        AnnotatedHarness harness;

        harness = (AnnotatedHarness) standardScriptRunner.getHarnesses().get(0);
        assertTrue("DB setup not invoked", ((DBSetupHarness) harness.getWrap()).setupCalled);

        //sub script
        ScriptLoaderProxy subScript = (ScriptLoaderProxy) standardScriptRunner.getHarnesses().get(1);
        assertEquals(2, subScript.getHarnesses().size());

        //back to main script
        harness = (AnnotatedHarness) standardScriptRunner.getHarnesses().get(2);
        assertTrue("Http run not invoked", ((HttpRunHarness) harness.getWrap()).runCalled);

        //db and http should have been called 2 times with these scripts!
        assertEquals(2, DBSetupHarness.callCount);
        assertEquals(2, HttpRunHarness.callCount);
    }


    public void testSubdir() throws Exception
    {
        System.setProperty("jdbc.url", "hey!");
        ScriptDefinitionLoader loader = new ScriptDefinitionLoader();
        SuiteLoader suiteLoader = new SuiteLoader();
        Suite suite = suiteLoader.loadSuite("org/mash/harness/testHarnessAndScript_data/suite1.xml");
        Script theScript = (Script) suite.getScriptOrParallel().get(1); //is the script3
        ScriptDefinition definition = loader.pullDefinition(theScript, suite);

        DBSetupHarness.reset();
        HttpRunHarness.reset();
        ScriptRunner runner = PropertyObjectFactory.getInstance().buildRunner();
        runner.run(definition);

        StandardScriptRunner standardScriptRunner = (StandardScriptRunner) runner;
        AnnotatedHarness harness;

        harness = (AnnotatedHarness) standardScriptRunner.getHarnesses().get(0);
        assertTrue("DB setup not invoked", ((DBSetupHarness) harness.getWrap()).setupCalled);

        //sub script
        ScriptLoaderProxy subScript = (ScriptLoaderProxy) standardScriptRunner.getHarnesses().get(1);
        assertEquals("script4", subScript.getName());
        assertEquals("someparam", subScript.getParameter().get(0).getName());

        subScript = (ScriptLoaderProxy) standardScriptRunner.getHarnesses().get(2);
        assertEquals("script5", subScript.getName());
        assertEquals("someparam", subScript.getParameter().get(0).getName());

        //back to main script
        harness = (AnnotatedHarness) standardScriptRunner.getHarnesses().get(3);
        assertTrue("Http run not invoked", ((HttpRunHarness) harness.getWrap()).runCalled);

        //db and http should have been called 2 times with these scripts!
        assertEquals(3, DBSetupHarness.callCount);
        assertEquals(3, HttpRunHarness.callCount);
    }
}
