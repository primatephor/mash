package org.mash.loader;

import junit.framework.TestCase;
import org.mash.config.Script;
import org.mash.config.ScriptDefinition;
import org.mash.config.Suite;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author:
 * @since: Jul 3, 2009
 */
public class TestScriptDefinitionLoader extends TestCase
{
    public void testDirLoad() throws Exception
    {
        ScriptDefinitionLoader loader = new ScriptDefinitionLoader();
        SuiteLoader suiteLoader = new SuiteLoader();
        Suite suite = suiteLoader.loadSuite("org/mash/loader/suite1.xml");
        List<ScriptDefinition> definitions = loader.pullDir("dir", suite);
        assertEquals(5, definitions.size());

        Set<String> names = new HashSet<String>();
        names.add(definitions.get(0).getName());
        names.add(definitions.get(1).getName());
        names.add(definitions.get(2).getName());
        names.add(definitions.get(3).getName());
        names.add(definitions.get(4).getName());

        assertTrue("'The Test' not in test definition", names.contains("The Test"));
        assertTrue("'Sample Setup' not in test definition", names.contains("Sample Setup"));
        assertTrue("'Sample 2 Setup' not in test definition", names.contains("Sample 2 Setup"));
        assertTrue("'The Second Test' not in test definition", names.contains("The Second Test"));
        assertTrue("'dir/unnamed_test.xml' not in test definition", names.contains("dir/unnamed_test.xml"));
    }

    public void testFileLoad() throws Exception
    {
        ScriptDefinitionLoader loader = new ScriptDefinitionLoader();
        SuiteLoader suiteLoader = new SuiteLoader();
        Suite suite = suiteLoader.loadSuite("org/mash/loader/suite2.xml");
        Script theScript = new Script();
        theScript.setFile("dir/test.xml");
        ScriptDefinition definition = loader.pullDefinition(theScript, suite);

        assertEquals("The Test", definition.getName());
    }

}
