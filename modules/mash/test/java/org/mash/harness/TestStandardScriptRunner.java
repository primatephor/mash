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
 * @author
 * @since Sep 19, 2009
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
        runner.run(definition, new HarnessContext());

        StandardScriptRunner standardScriptRunner = (StandardScriptRunner) runner;
        assertEquals(4, standardScriptRunner.getHarnesses().size());
        AnnotatedHarness harness;

        harness = (AnnotatedHarness) standardScriptRunner.getHarnesses().get(0);
        assertTrue("DB setup not invoked", ((DBSetupHarness) harness.getWrap()).setupCalled);

        //sub script
        ScriptLoaderProxy subScript = (ScriptLoaderProxy) standardScriptRunner.getHarnesses().get(1);
        assertEquals(2, subScript.getHarnesses().size());

        //back to main script
        harness = (AnnotatedHarness) standardScriptRunner.getHarnesses().get(2);
        assertTrue("Http run not invoked", ((HttpRunHarness) harness.getWrap()).runCalled);

        //verify
        AnnotatedHarness verify = (AnnotatedHarness) standardScriptRunner.getHarnesses().get(3);
        assertTrue("Verify harness not run", ((HttpVerifyHarness)verify.getWrap()).verifyCalled);

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
        runner.run(definition, new HarnessContext());

        StandardScriptRunner standardScriptRunner = (StandardScriptRunner) runner;
        AnnotatedHarness harness;

        harness = (AnnotatedHarness) standardScriptRunner.getHarnesses().get(0);
        assertTrue("DB setup not invoked", ((DBSetupHarness) harness.getWrap()).setupCalled);

        //sub script
        //because a directory was called, scripts may come in any order in that directory
        ScriptLoaderProxy subScript1 = (ScriptLoaderProxy) standardScriptRunner.getHarnesses().get(1);
        ScriptLoaderProxy subScript2 = (ScriptLoaderProxy) standardScriptRunner.getHarnesses().get(2);
        if(!"script4".equals(subScript1.getName())){
            subScript1 = (ScriptLoaderProxy) standardScriptRunner.getHarnesses().get(2);
            subScript2 = (ScriptLoaderProxy) standardScriptRunner.getHarnesses().get(1);
        }
        assertEquals("script4", subScript1.getName());
        assertEquals("someparam", subScript1.getParameter().get(0).getName());

        assertEquals("script5", subScript2.getName());
        assertEquals("someparam", subScript2.getParameter().get(0).getName());

        //back to main script
        harness = (AnnotatedHarness) standardScriptRunner.getHarnesses().get(3);
        assertTrue("Http run not invoked", ((HttpRunHarness) harness.getWrap()).runCalled);

        //db and http should have been called 2 times with these scripts!
        assertEquals(3, DBSetupHarness.callCount);
        assertEquals(3, HttpRunHarness.callCount);
    }
}
