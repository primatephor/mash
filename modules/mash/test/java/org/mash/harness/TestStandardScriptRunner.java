package org.mash.harness;

import junit.framework.TestCase;
import org.mash.config.ScriptDefinition;
import org.mash.config.Suite;
import org.mash.loader.ScriptDefinitionLoader;
import org.mash.loader.SuiteLoader;

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
        Suite suite = suiteLoader.loadSuite("tetsHarnessAndScript_data/suite1.xml");
        ScriptDefinition definition = loader.pullFile("tetsHarnessAndScript_data/scirpt1.xml", suite);

        ScriptRunner runner = RunnerFactory.getInstance().buildRunner();
        runner.run(definition);
    }
}
