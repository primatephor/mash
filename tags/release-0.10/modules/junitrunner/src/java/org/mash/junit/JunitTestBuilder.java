package org.mash.junit;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.mash.config.ScriptDefinition;
import org.mash.config.Suite;
import org.mash.loader.ScriptDefinitionLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Construct the test cases applicable to the suite.  Each suite is definied by the tests inside of it, but not all
 * tests may get added to the suite.  For instance, if tags are specified this class will analyze them and determine if
 * they should be added.
 * <p/>
 * Tests referenced by a file or directory are not actually instantiated here, they're meta data is loaded and when the
 * test is run, then the test is loaded.  This is an attempt to keep the memory footprint low should there be thousands
 * of tests.
 * <p/>
 * User: teastlack Date: Jul 1, 2009 Time: 3:06:56 PM
 */
public class JunitTestBuilder
{
    private static final Logger log = Logger.getLogger(JunitTestBuilder.class.getName());
    private ScriptDefinitionLoader scriptDefinitionLoader;
    private Suite theSuite;

    public JunitTestBuilder()
    {
        this.scriptDefinitionLoader = new ScriptDefinitionLoader();
    }

    public List<Test> buildTests(Suite suite)
    {
        return buildTests(suite, null);
    }

    public List<Test> buildTests(Suite suite, List<String> tags)
    {
        this.theSuite = suite;
        List<Test> results = new ArrayList<Test>();
        for (Object testDefinition : this.theSuite.getScriptOrParallel())
        {
            if (testDefinition instanceof ScriptDefinition)
            {
                results.addAll(buildTests((ScriptDefinition) testDefinition, tags));
            }
        }
        return results;
    }


    /**
     * Create a list of junit TestCases from the supplied TestDefinition.  This may create a list because the test may
     * reference a directory, and all tests within that directory are checked for addition against the supplied tags.
     *
     * @param definition to load
     * @param tags       to check against a test to see if it should be added
     * @return testcases to add to a suite
     */
    private List<junit.framework.Test> buildTests(ScriptDefinition definition, List<String> tags)
    {
        List<junit.framework.Test> result = new ArrayList<junit.framework.Test>();
        log.info("Attempting to add definition");
        result.addAll(createStandardTests(definition, tags));
        return result;
    }

    private List<junit.framework.Test> createStandardTests(ScriptDefinition scriptDefinition, List<String> tags)
    {
        List<junit.framework.Test> result = new ArrayList<junit.framework.Test>();
        try
        {
            //load the definition outright, as it's a fully defined test
            if (scriptDefinition.getFile() == null &&
                scriptDefinition.getDir() == null)
            {
                if (checktags(scriptDefinition, tags))
                {
                    result.add(new StandardTestCase(scriptDefinition));
                }
            }
            else
            {
                TestSuite suite = new TestSuite();
                if(scriptDefinition.getDir() != null)
                {
                    suite.setName(scriptDefinition.getDir());
                }
                //load the directory of tests
                List<ScriptDefinition> scripts = scriptDefinitionLoader.pullSubDefinitions(scriptDefinition, theSuite.getPath());
                for (ScriptDefinition script : scripts)
                {
                    if (checktags(script, tags))
                    {
                        suite.addTest(new StandardTestCase(script));
                    }
                }
                result.add(suite);
            }
        }
        catch (Exception e)
        {
            log.error("Unexpected error building standard tests", e);
        }
        return result;
    }


    /**
     * Check to see if the test definition contains the tags specified.  Used to determine if we can add as a test.
     *
     * @param script definition to check
     * @param tags   to check against
     * @return true if definition should be added, false otherwise
     */
    protected boolean checktags(ScriptDefinition script, List<String> tags)
    {
        Boolean result = false;
        if (tags == null || tags.size() == 0)
        {
            result = true;
        }
        else
        {
            for (String tag : tags)
            {
                if (script.getTag().contains(tag))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
