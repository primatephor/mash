package org.mash.loader;

import junit.framework.TestCase;
import org.mash.config.Suite;
import org.mash.config.ScriptDefinition;

import java.util.List;

/**
 * @author: teastlack
 * @since: Jul 3, 2009
 */
public class TestScriptLoaderProxy extends TestCase
{
    public void testLoading() throws Exception
    {
        SuiteLoader suiteLoader = new SuiteLoader();
        Suite suite = suiteLoader.loadSuite("org/mash/junit/suite.xml");

        assertEquals(2, suite.getScriptOrParallel().size());
        List<ScriptDefinition> scripts = new ScriptDefinitionLoader().pullDir("suite", suite);
        ScriptLoaderProxy proxy = (ScriptLoaderProxy) scripts.get(0);
        assertFalse(proxy.isTestLoaded());
        assertEquals(3, proxy.getTag().size());
        assertFalse(proxy.isTestLoaded());
        assertEquals("The Test", proxy.getName());
        assertFalse(proxy.isTestLoaded());
        assertEquals(3, proxy.getHarnesses().size());
        assertTrue(proxy.isTestLoaded());
    }
}
