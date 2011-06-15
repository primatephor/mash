package org.mash.loader;

import junit.framework.TestCase;
import org.mash.config.Configuration;
import org.mash.config.Script;
import org.mash.config.ScriptDefinition;
import org.mash.config.Setup;
import org.mash.config.Suite;
import org.mash.harness.AnnotatedDBSetupHarness;
import org.mash.harness.Harness;
import org.mash.loader.harnesssetup.AnnotatedHarness;
import org.mash.loader.harnesssetup.CalculatingConfigBuilder;

import java.util.List;

/**
 *
 * @author teastlack
 * @since Jul 9, 2009 5:22:24 PM
 *
 */
public class TestHarnessBuilder extends TestCase
{
    public void testConfigLoad() throws Exception
    {
        System.setProperty("jdbc.url", "hey!");
        ScriptDefinitionLoader loader = new ScriptDefinitionLoader();
        SuiteLoader suiteLoader = new SuiteLoader();
        Suite suite = suiteLoader.loadSuite("org/mash/loader/suite1.xml");
        Script theScript = new Script();
        theScript.setFile("dir/setup.xml");
        ScriptDefinition definition = loader.pullDefinition(theScript, suite);

        assertEquals("Sample Setup", definition.getName());
        List harnesses = definition.getHarnesses();
        Setup harness = (Setup) harnesses.get(0);
        assertEquals("type", harness.getConfiguration().get(0).getName());
        assertEquals("DELETE", harness.getConfiguration().get(0).getValue());
        assertEquals(null, harness.getConfiguration().get(1).getValue());

        Harness toCheck = new HarnessBuilder().buildHarness(harness);
        CalculatingConfigBuilder configurationBuilder = new CalculatingConfigBuilder();
        List<Configuration> configs = configurationBuilder.applyParameters(null, definition, toCheck.getDefinition());
        toCheck.setConfiguration(configs);
        assertEquals("hey!", toCheck.getConfiguration().get(1).getValue());
    }

    public void testAnnotatedConfig() throws Exception
    {
        System.setProperty("jdbc.url", "hey!");
        ScriptDefinitionLoader loader = new ScriptDefinitionLoader();
        SuiteLoader suiteLoader = new SuiteLoader();
        Suite suite = suiteLoader.loadSuite("org/mash/loader/suite1.xml");
        Script theScript = new Script();
        theScript.setFile("dir/setup2.xml");
        ScriptDefinition definition = loader.pullDefinition(theScript, suite);

        assertEquals("Sample 2 Setup", definition.getName());
        List harnesses = definition.getHarnesses();
        Setup harness = (Setup) harnesses.get(0);
        assertEquals("type", harness.getConfiguration().get(0).getName());
        assertEquals("DELETE", harness.getConfiguration().get(0).getValue());
        assertEquals(null, harness.getConfiguration().get(1).getValue());

        Harness toCheck = new HarnessBuilder().buildHarness(harness);
        CalculatingConfigBuilder configurationBuilder = new CalculatingConfigBuilder();
        List<Configuration> configs = configurationBuilder.applyParameters(null, definition, toCheck.getDefinition());
        toCheck.setConfiguration(configs);

        AnnotatedDBSetupHarness db = (AnnotatedDBSetupHarness) ((AnnotatedHarness) toCheck).getWrap();
        assertEquals("hey!", db.getUrl());
        //not set intentionally
        assertEquals(null, db.getPass());
    }

}
