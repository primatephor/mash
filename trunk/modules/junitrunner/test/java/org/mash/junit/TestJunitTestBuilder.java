package org.mash.junit;

import junit.framework.Test;
import junit.framework.TestCase;
import org.mash.config.Suite;
import org.mash.loader.SuiteLoader;

import java.util.Arrays;
import java.util.List;

/**
 * @author: teastlack
 * @since: Jul 3, 2009
 */
public class TestJunitTestBuilder extends TestCase
{
    public void testDefinitionLoad() throws Exception
    {
        SuiteLoader suiteLoader = new SuiteLoader();
        Suite suite = suiteLoader.loadSuite("org/mash/junit/suite.xml");
        JunitTestBuilder builder = new JunitTestBuilder();
        List<Test> tests = builder.buildTests(suite);

        assertEquals(2, suite.getScriptOrParallel().size());
        assertEquals(3, tests.size());
        StandardTestCase test = (StandardTestCase) tests.get(0);
        assertEquals("Base Test", test.getTestDefinition().getName());
        test = (StandardTestCase) tests.get(1);
        assertEquals("The Test", test.getTestDefinition().getName());
        test = (StandardTestCase) tests.get(2);
        assertEquals("The Second Test", test.getTestDefinition().getName());
    }

    public void testDirectoryTags() throws Exception
    {
        SuiteLoader suiteLoader = new SuiteLoader();
        Suite suite = suiteLoader.loadSuite("org/mash/junit/suite.xml");
        JunitTestBuilder builder = new JunitTestBuilder();
        List<String> tags = Arrays.asList("login", "website");
        List<Test> tests = builder.buildTests(suite, tags);

        assertEquals(2, suite.getScriptOrParallel().size());
        assertEquals(3, tests.size());
        StandardTestCase test = (StandardTestCase) tests.get(0);
        assertEquals("Base Test", test.getTestDefinition().getName());
        test = (StandardTestCase) tests.get(1);
        assertEquals("The Test", test.getTestDefinition().getName());
        test = (StandardTestCase) tests.get(2);
        assertEquals("The Second Test", test.getTestDefinition().getName());


        tags = Arrays.asList("mercury");
        tests = builder.buildTests(suite, tags);

        assertEquals(2, suite.getScriptOrParallel().size());
        assertEquals(2, tests.size());
        test = (StandardTestCase) tests.get(0);
        assertEquals("Base Test", test.getTestDefinition().getName());
        test = (StandardTestCase) tests.get(1);
        assertEquals("The Test", test.getTestDefinition().getName());
    }
}
