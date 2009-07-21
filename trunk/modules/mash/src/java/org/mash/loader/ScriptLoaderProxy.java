package org.mash.loader;

import org.apache.log4j.Logger;
import org.mash.config.Suite;
import org.mash.config.Script;
import org.mash.config.ScriptDefinition;

import java.io.File;
import java.util.List;

/**
 * This proxy is designed to help with the memory footprint a bit once there are thousands of tests.  Basically, it
 * maintains the filename, and retrieves the tags for examining if the test is valid.  Once a user starts accessing the
 * various elements of the test, the test is loaded.
 * <p/>
 * While this necessarily results in tests being loaded twice, they're not fully maintained in memory whilst the suite
 * is being run or tags are analyzed.
 * <p/>
 * User: teastlack Date: Jul 2, 2009 Time: 3:32:58 PM
 */
public class ScriptLoaderProxy implements ScriptDefinition
{
    private static final Logger log = Logger.getLogger(ScriptLoaderProxy.class.getName());

    private String filename;
    private String dir;
    private String name;
    private Script script;
    private List<String> tags;
    private Boolean validTestFile = true;
    private Suite suite;
    private File path;

    public ScriptLoaderProxy(String filename, Suite suite)
    {
        this.filename = filename;
        this.suite = suite;
        Script temp = loadTest(this.filename);
        if (temp != null)
        {
            this.tags = temp.getTag();
            this.name = temp.getName();
            this.dir = temp.getDir();
        }
        else
        {
            this.validTestFile = false;
        }
    }

    public List<String> getTag()
    {
        return this.tags;
    }

    public List<Object> getHarnesses()
    {
        return this.getTest() != null ? this.getTest().getHarnesses() : null;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDir()
    {
        return this.dir;
    }

    public String getFile()
    {
        return this.filename;
    }

    public Script getTest()
    {
        if (script == null)
        {
            script = loadTest(this.filename);
        }
        return script;
    }

    public File getPath()
    {
        return this.path;
    }

    public Boolean isTestLoaded()
    {
        return this.script != null;
    }

    public Boolean isValidTestFile()
    {
        return validTestFile;
    }

    private Script loadTest(String filename)
    {
        log.debug("Loading file " + filename);
        Script result = null;
        if (filename != null)
        {
            try
            {
                FileLoader loader = new FileLoader();
                path = loader.findFile(filename, suite.getPath());
                TextFileReader reader = new TextFileReader();
                String contents = reader.getContents(path);
                JAXBSuiteMarshaller marshaller = new JAXBSuiteMarshaller();
                result = (Script) marshaller.unmarshal(contents);
                result.setPath(path);
            }
            catch (Exception e)
            {
                log.debug("Invalid Test! Unexpected error loading test instance:" + e.getMessage());
                this.validTestFile = false;
            }
        }
        return result;

    }
}
