package org.mash.loader;

import org.apache.log4j.Logger;
import org.mash.config.Suite;
import org.mash.config.ScriptDefinition;

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
        List<ScriptDefinition> scripts = new ArrayList<ScriptDefinition>();
        if (directory != null)
        {
            File dir = fileLoader.findFile(directory, suite.getPath());
            log.info("Loading files from directory " + dir.getAbsolutePath());
            File[] files = dir.listFiles();
            for (File file : files)
            {
                ScriptDefinition pulled = pullFile(file.getAbsolutePath(), suite);
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
        ScriptLoaderProxy proxy = new ScriptLoaderProxy(filename, suite);
        if (proxy.isValidTestFile())
        {
            return proxy;
        }
        return null;
    }
}
