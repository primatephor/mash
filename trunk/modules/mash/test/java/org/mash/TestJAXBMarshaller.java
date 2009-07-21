package org.mash;

import junit.framework.TestCase;
import org.mash.config.Configuration;
import org.mash.config.Parallel;
import org.mash.config.Parameter;
import org.mash.config.Replace;
import org.mash.config.Run;
import org.mash.config.Script;
import org.mash.config.Setup;
import org.mash.config.Suite;
import org.mash.config.Verify;
import org.mash.loader.FileReaderException;
import org.mash.loader.JAXBSuiteMarshaller;
import org.mash.loader.SuiteMarshallerException;
import org.mash.loader.TextFileReader;


/**
 * User: teastlack Date: Jul 1, 2009 Time: 11:41:53 AM
 */
public class TestJAXBMarshaller extends TestCase
{
    public void testSuiteLoad() throws FileReaderException, SuiteMarshallerException
    {
        TextFileReader reader = new TextFileReader();
        String contents = reader.getContents("org/mash/junit/suite/suite.xml");
        JAXBSuiteMarshaller marshaller = new JAXBSuiteMarshaller();
        Suite suite = (Suite) marshaller.unmarshal(contents);
        assertEquals("The Suite", suite.getName());

        Script script = (Script) suite.getScriptOrParallel().get(0);
        assertEquals("dir1/TestA.xml", script.getFile());

        script = (Script) suite.getScriptOrParallel().get(1);
        assertEquals("dir2", script.getDir());

        Parallel parallel = (Parallel) suite.getScriptOrParallel().get(2);
        script = parallel.getScript().get(0);
        assertEquals("dir3/TestB.xml", script.getFile());
        script = parallel.getScript().get(1);
        assertEquals("dir4/TestC.xml", script.getFile());

        script = (Script) suite.getScriptOrParallel().get(3);
        assertEquals("The Test", script.getName());
        assertEquals("login", script.getTag().get(0));
        assertEquals("website", script.getTag().get(1));
        assertEquals("myapp", script.getTag().get(2));

        Setup setup = (Setup) script.getHarnesses().get(0);
        assertEquals("org.mash.harness.db.DBSetupHarness", setup.getType());
        Configuration config = setup.getConfiguration().get(0);
        assertEquals("clean", config.getName());
        assertEquals("true", config.getValue());
        Parameter param = setup.getParameter().get(0);
        assertEquals("loadfile", param.getName());
        assertEquals("db_load.xml", param.getFile());
        Replace replace = param.getReplace().get(0);
        assertEquals("$variable$", replace.getSearch());
        assertEquals("sometext", replace.getValue());

        //just check that run and verify are there
        Run run = (Run) script.getHarnesses().get(1);
        assertEquals("org.mash.harness.http.HttpRunHarness", run.getType());

        Verify verify = (Verify) script.getHarnesses().get(2);
        assertEquals("org.mash.harness.http.HttpVerifyHarness", verify.getType());
    }

    public void testTestLoad() throws FileReaderException, SuiteMarshallerException
    {
        TextFileReader reader = new TextFileReader();
        String contents = reader.getContents("org/mash/junit/suite/test.xml");
        JAXBSuiteMarshaller marshaller = new JAXBSuiteMarshaller();
        Script script = (Script) marshaller.unmarshal(contents);

        assertEquals("The Test", script.getName());
        assertEquals("login", script.getTag().get(0));
        assertEquals("website", script.getTag().get(1));
        assertEquals("myapp", script.getTag().get(2));

        Setup setup = (Setup) script.getHarnesses().get(0);
        assertEquals("org.mash.harness.db.DBSetupHarness", setup.getType());
        Configuration config = setup.getConfiguration().get(0);
        assertEquals("clean", config.getName());
        assertEquals("true", config.getValue());
        Parameter param = setup.getParameter().get(0);
        assertEquals("loadfile", param.getName());
        assertEquals("db_load.xml", param.getFile());
        Replace replace = param.getReplace().get(0);
        assertEquals("$variable$", replace.getSearch());
        assertEquals("sometext", replace.getValue());

        //just check that run and verify are there
        Run run = (Run) script.getHarnesses().get(1);
        assertEquals("org.mash.harness.http.HttpRunHarness", run.getType());

        Verify verify = (Verify) script.getHarnesses().get(2);
        assertEquals("org.mash.harness.http.HttpVerifyHarness", verify.getType());
    }
}
