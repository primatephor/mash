package org.mash.loader;

import junit.framework.TestCase;
import org.mash.config.Setup;
import org.mash.config.Suite;
import org.mash.config.ScriptDefinition;
import org.mash.harness.BaseHarness;

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
        ScriptDefinition definition = loader.pullFile("dir/setup.xml", suite);

        assertEquals("Sample Setup", definition.getName());
        List harnesses = definition.getHarnesses();
        Setup harness = (Setup) harnesses.get(0);
        assertEquals("type", harness.getConfiguration().get(0).getName());
        assertEquals("DELETE", harness.getConfiguration().get(0).getValue());
        assertEquals(null, harness.getConfiguration().get(1).getValue());

        BaseHarness toCheck = (BaseHarness) new HarnessBuilder().buildHarness(harness, definition);
        assertEquals("hey!", toCheck.getConfiguration().get(1).getValue());
    }

}
