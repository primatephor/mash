package org.mash.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.config.Script;
import org.mash.config.ScriptDefinition;
import org.mash.config.Suite;
import org.mash.file.FileLoader;
import org.mash.file.FileReaderException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Load the definitions from the filesystem.
 * <p/>
 * This allows tests to be loaded relative to the test suite definition file, or have absolute paths definied within the
 * test files themselves.
 *
 * @author
 * @since Jul 3, 2009
 */
public class ScriptDefinitionLoader
{
    private static final Logger log = LogManager.getLogger(ScriptDefinitionLoader.class.getName());
    private FileLoader fileLoader;
    private List<String> tags;

    public ScriptDefinitionLoader()
    {
        this.fileLoader = new FileLoader();
    }

    public ScriptDefinitionLoader(List<String> tags)
    {
        this();
        this.tags = tags;
    }

    /**
     * Load the directory of subscripts of this script definition.
     *
     * @param scriptDefinition to check for referenced scripts in
     * @param basePath         of the suite (or whereever the base has been run)
     * @return list of definitions
     * @throws FileReaderException      when unable to read a file
     * @throws SuiteMarshallerException when unable to marshal file
     */
    public List<ScriptDefinition> pullSubDefinitions(ScriptDefinition scriptDefinition, File basePath)
            throws SuiteMarshallerException, FileReaderException
    {
        List<ScriptDefinition> result = new ArrayList<ScriptDefinition>();
        ScriptDefinition top = pullDefinition(scriptDefinition, basePath);
        if (top != null)
        {
            result.add(top);
        }
        //load the directory of tests
        String dirname = scriptDefinition.getDir();
        if (dirname != null)
        {
            List<ScriptDefinition> scripts = pullDir(dirname, basePath);
            for (ScriptDefinition script : scripts)
            {
                script.getParameter().addAll(scriptDefinition.getParameter());
                result.add(script);
            }
        }
        return result;
    }


    public ScriptDefinition pullDefinition(ScriptDefinition scriptDefinition, Suite suite)
            throws SuiteMarshallerException, FileReaderException
    {
        return pullDefinition(scriptDefinition, suite.getPath());
    }

    /**
     * Load the specific subscript in this definition
     *
     * @param scriptDefinition to retrieve
     * @param basePath         of start point
     * @return defined script
     * @throws SuiteMarshallerException when xml is invalid
     * @throws FileReaderException      when file not found
     */
    public ScriptDefinition pullDefinition(ScriptDefinition scriptDefinition, File basePath)
            throws SuiteMarshallerException, FileReaderException
    {
        ScriptDefinition result = null;
        String filename = scriptDefinition.getFile();
        if (filename != null)
        {
            ScriptLoaderProxy proxy = new ScriptLoaderProxy(filename, basePath);
            if (proxy.isValidTestFile() && checktags(proxy))
            {
                result = proxy;
                result.getParameter().addAll(scriptDefinition.getParameter());
            }
        }
        return result;
    }

    /**
     * Pull all the definitions within a directory
     *
     * @param directory to retrieve tests from
     * @param suite     of all tests
     * @return list of test definitions
     * @throws FileReaderException      when unable to read a file
     * @throws SuiteMarshallerException when unable to marshal file
     */
    public List<ScriptDefinition> pullDir(String directory, Suite suite)
            throws FileReaderException, SuiteMarshallerException
    {
        return pullDir(directory, suite.getPath());
    }

    public List<ScriptDefinition> pullDir(String directory, File basePath)
            throws FileReaderException, SuiteMarshallerException
    {
        List<ScriptDefinition> scripts = new ArrayList<ScriptDefinition>();
        if (directory != null)
        {
            File dir = fileLoader.findFile(directory, basePath);
            log.info("Loading files from directory " + dir.getAbsolutePath());
            File[] files = dir.listFiles();
            if (files == null)
            {
                throw new IllegalArgumentException("No files under directory " + dir.getAbsolutePath());
            }
            else
            {
                for (File file : files)
                {
                    if (file.getAbsolutePath().toLowerCase().endsWith(".xml"))
                    {
                        Script theScript = new Script();
                        theScript.setFile(file.getAbsolutePath());
                        ScriptDefinition pulled = pullDefinition(theScript, basePath);
                        if (pulled != null)
                        {
                            scripts.add(pulled);
                        }
                    }
                    else
                    {
                        log.info("Unknown file type:" + file.getAbsolutePath());
                    }
                }
            }
        }
        return scripts;
    }

    /**
     * Check to see if the test definition contains the tags specified.  Used to determine if we can add as a test.
     *
     * @param script definition to check
     * @return true if definition should be added, false otherwise
     */
    protected boolean checktags(ScriptDefinition script)
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
