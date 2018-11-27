package org.mash.loader;

import junit.framework.TestCase;
import org.mash.config.ScriptDefinition;
import org.mash.config.Suite;

import java.util.List;

/**
 * @author:
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
        ScriptLoaderProxy firstTest = (ScriptLoaderProxy) scripts.get(0);
        ScriptLoaderProxy secondTest = (ScriptLoaderProxy) scripts.get(1);
        if(!"The Test".equals(firstTest.getName())) {
            firstTest = secondTest;
            secondTest = (ScriptLoaderProxy) scripts.get(0);
        }
        assertFalse(firstTest.isTestLoaded());
        assertEquals(3, firstTest.getTag().size());
        assertFalse(firstTest.isTestLoaded());
        assertEquals("The Test", firstTest.getName());
        assertFalse(firstTest.isTestLoaded());
        assertEquals(3, firstTest.getHarnesses().size());
        assertTrue(firstTest.isTestLoaded());

        assertFalse(secondTest.isTestLoaded());
        assertEquals(2, secondTest.getTag().size());
        assertFalse(secondTest.isTestLoaded());
        assertEquals("The Second Test", secondTest.getName());
        assertFalse(secondTest.isTestLoaded());
        assertEquals(3, secondTest.getHarnesses().size());
        assertTrue(secondTest.isTestLoaded());
    }
}
