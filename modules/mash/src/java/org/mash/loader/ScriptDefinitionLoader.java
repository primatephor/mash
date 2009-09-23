package org.mash.loader;

import org.apache.log4j.Logger;
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
 * @author: teastlack
 * @since: Jul 3, 2009
 */
public class ScriptDefinitionLoader
{
    private static final Logger log = Logger.getLogger(ScriptDefinitionLoader.class.getName());
    private FileLoader fileLoader;

    public ScriptDefinitionLoader()
    {
        this.fileLoader = new FileLoader();
    }

    /**
     * Load the subscripts of this script definition.
     *
     * @param scriptDefinition to check for referenced scripts in
     * @param basePath         of the suite (or whereever the base has been run)
     * @return list of definitions
     * @throws FileReaderException      when unable to read a file
     * @throws SuiteMarshallerException when unable to marshal file
     */
    public List<ScriptDefinition> pullSubDefinitions(ScriptDefinition scriptDefinition, File basePath) throws SuiteMarshallerException, FileReaderException
    {
        List<ScriptDefinition> result = new ArrayList<ScriptDefinition>();
        String filename = scriptDefinition.getFile();
        ScriptDefinition fileDef = pullFile(filename, basePath);
        if (fileDef != null)
        {
            result.add(fileDef);
        }
        //load the directory of tests
        String dirname = scriptDefinition.getDir();
        if (dirname != null)
        {
            List<ScriptDefinition> scripts = pullDir(dirname, basePath);
            for (ScriptDefinition script : scripts)
            {
                result.add(script);
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
    public List<ScriptDefinition> pullDir(String directory, Suite suite) throws FileReaderException, SuiteMarshallerException
    {
        return pullDir(directory, suite.getPath());
    }

    public List<ScriptDefinition> pullDir(String directory, File basePath) throws FileReaderException, SuiteMarshallerException
    {
        List<ScriptDefinition> scripts = new ArrayList<ScriptDefinition>();
        if (directory != null)
        {
            File dir = fileLoader.findFile(directory, basePath);
            log.info("Loading files from directory " + dir.getAbsolutePath());
            File[] files = dir.listFiles();
            for (File file : files)
            {
                ScriptDefinition pulled = pullFile(file.getAbsolutePath(), basePath);
                if (pulled != null)
                {
                    scripts.add(pulled);
                }
            }
        }
        return scripts;
    }

    /**
     * Pull the definition specified by the filename
     *
     * @param filename to retrieve tests from
     * @param suite    of all tests
     * @return list of test definitions
     * @throws FileReaderException      when unable to read a file
     * @throws SuiteMarshallerException when unable to marshal file
     */
    public ScriptDefinition pullFile(String filename, Suite suite) throws FileReaderException, SuiteMarshallerException
    {
        return pullFile(filename, suite.getPath());
    }

    public ScriptDefinition pullFile(String filename, File suitePath) throws FileReaderException, SuiteMarshallerException
    {
        ScriptLoaderProxy proxy = new ScriptLoaderProxy(filename, suitePath);
        if (proxy.isValidTestFile())
        {
            return proxy;
        }
        return null;
    }
}
