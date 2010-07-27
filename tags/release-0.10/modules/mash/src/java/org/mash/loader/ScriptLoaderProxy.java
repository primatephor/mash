package org.mash.loader;

import org.apache.log4j.Logger;
import org.mash.config.Script;
import org.mash.config.ScriptDefinition;
import org.mash.config.Suite;
import org.mash.file.FileLoader;
import org.mash.file.TextFileReader;

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
    private File path;
    private File suitePath;

    public ScriptLoaderProxy(String filename, File suitePath)
    {
        this.filename = filename;
        this.suitePath = suitePath;
        Script temp = loadTest(this.filename);
        if (temp != null)
        {
            this.tags = temp.getTag();
            this.name = temp.getName();
            calculateName(filename, suitePath);
            this.dir = temp.getDir();
        }
        else
        {
            this.validTestFile = false;
        }
    }

    /**
     * Names can be interesting. By default, it's set to whatever the attribute name was chosen.  However
     * this may not be set, so here's where we calculate using the filename, subtracting the suite path.
     *
     * @param filename of test
     * @param suitePath of test suite
     */
    protected void calculateName(String filename, File suitePath)
    {
        if(this.name == null)
        {
            this.name = filename;
            if(suitePath != null && this.name != null)
            {
                int start = this.name.indexOf(suitePath.getParent());
                if(start >= 0)
                {
                    if(this.name.length() > suitePath.getParent().length())
                    {
                        this.name = this.name.substring(suitePath.getParent().length()+1);
                    }
                    else
                    {
                        this.name = this.name.substring(suitePath.getParent().length());
                    }
                }
            }
        }
    }

    public ScriptLoaderProxy(String filename, Suite suite)
    {
        this(filename, suite.getPath());
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
                path = loader.findFile(filename, suitePath);
                TextFileReader reader = new TextFileReader();
                if (!path.isDirectory())
                {
                    String contents = reader.getContents(path);
                    JAXBSuiteMarshaller marshaller = new JAXBSuiteMarshaller();
                    Object xmlData = marshaller.unmarshal(contents);
                    if (xmlData instanceof Script)
                    {
                        result = (Script) xmlData;
                        result.setPath(path);
                    }
                }
                else
                {
                    log.debug("File '" + filename + "' with path " + path + " is a directory");
                }
            }
            catch (Exception e)
            {
                log.error("Problem loading test! Unexpected error loading file '" + filename + "' ", e);
                this.validTestFile = false;
            }
        }
        return result;

    }
}
