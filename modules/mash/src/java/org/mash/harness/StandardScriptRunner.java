package org.mash.harness;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.config.HarnessDefinition;
import org.mash.config.Script;
import org.mash.config.ScriptDefinition;
import org.mash.loader.HarnessBuilder;
import org.mash.loader.ScriptDefinitionLoader;
import org.mash.tool.ErrorFormatter;
import org.mash.tool.ErrorHandler;
import org.mash.tool.StringUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This runs through and invokes the harnesses.  Each test framework implementation (like JUnit) would call this piece
 * and behave appropriately based on the test errors.  Each harness could have errors, and will these errors will be
 * returned after a harness is run.
 * <p/>
 * The setup, run, verify, and teardown are invoked separately.  A runner could be extended to modify how these are
 * invoked here.
 * <p/>
 *  Date: Jul 7, 2009 Time: 3:23:20 PM
 */
public class StandardScriptRunner implements ScriptRunner
{
    private static final Logger log = LogManager.getLogger(StandardScriptRunner.class.getName());
    protected List harnesses;
    private HarnessContext context;

    public static void main(String[] args) throws Exception
    {
        if (args.length < 1)
        {
            System.out.println("Args: <script file name>");
            System.out.println("Please insert a script filename");
            System.exit(0);
        }

        String fileName = args[0];
        log.info("Running script " + fileName);

        if (log.isTraceEnabled())
        {
            ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
            URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();
            for (URL url : urls)
            {
                log.trace("classpath:" + url.getFile());
            }
        }
        StandardScriptRunner runner = new StandardScriptRunner();

        Script theScript = new Script();
        theScript.setFile(fileName);
        ScriptDefinition definition = new ScriptDefinitionLoader().pullDefinition(theScript, new File("."));

        if (definition != null)
        {
            ErrorHandler handler =
                    new ErrorHandler((ErrorFormatter) PropertyObjectFactory.getInstance().buildFormatter());
            handler.handleErrors(runner.run(definition, new HarnessContext()), definition);
            if (handler.isError())
            {
                log.error("There were errors running script");
            }
            else
            {
                log.info("No errors");
            }
        }
        else
        {
            log.error("Unable to find script to run:" + new File(fileName).getAbsolutePath());
        }
    }

    /**
     * Construct the harnesses to be run from the list of definitions here.
     *
     *
     * @param definition defined by the configurations
     * @return harnesses to be run
     * @throws Exception upon error
     */
    protected List buildHarnesses(ScriptDefinition definition) throws Exception
    {
        List results = new ArrayList();
        HarnessBuilder builder = HarnessBuilder.getInstance();
        ScriptDefinitionLoader loader = new ScriptDefinitionLoader();
        if (definition.getHarnesses() != null)
        {
            for (Object current : definition.getHarnesses())
            {
                if (current instanceof HarnessDefinition)
                {
                    HarnessDefinition harnessDefinition = (HarnessDefinition) current;
                    harnessDefinition.setScriptDefinition(definition);
                    log.info("Configuring " + harnessDefinition.getName());
                    Harness toAdd = builder.buildHarness(harnessDefinition);
                    results.add(toAdd);
                    log.trace("Done configuring " + harnessDefinition.getName());
                }
                else if (current instanceof ScriptDefinition)
                {
                    ScriptDefinition scriptDefinition = (ScriptDefinition) current;
                    List<ScriptDefinition> toAdd = loader.pullSubDefinitions(scriptDefinition, definition.getPath());
                    for (ScriptDefinition sub : toAdd)
                    {
                        log.info("Loading script " +
                                         StringUtil.cleanNull(sub.getDir()) + "/" +
                                         StringUtil.cleanNull(sub.getFile()));
                        results.add(sub);
                    }
                }
                else
                {
                    log.warn("Unable to apply configurations to " + current.getClass().getName() +
                                     ", not a HarnessDefinition or ScriptDefinition");
                }
            }
        }
        log.trace("Completed building harnesses and configurations");
        return results;
    }

    /**
     * Override the runTest method, which simply looks at the junit name and invokes that name as the method.  Since we
     * know the method to invoke, and it's cleaner to call it straight away, just invoke the method directly.
     *
     * @param definition of the test to run (containing harnesses to run)
     * @return list of errors resulting from verify
     * @throws Exception when error occurs
     */
    public List<HarnessError> run(ScriptDefinition definition, HarnessContext context) throws Exception
    {
        this.context = context;
        List<HarnessError> errors = new ArrayList<HarnessError>();

        try
        {
            this.harnesses = buildHarnesses(definition);
            ScriptDefinitionLoader loader = new ScriptDefinitionLoader();

            if (this.harnesses != null)
            {
                log.debug("Running test");
                for (Object toRun : this.harnesses)
                {
                    if (toRun instanceof Harness)
                    {
                        Harness harness = (Harness) toRun;
                        HarnessRunner runner = PropertyObjectFactory.getInstance().buildHarnessRunner();
                        errors.addAll(runner.run(definition, harness, context));
                    }
                    else if (toRun instanceof ScriptDefinition)
                    {
                        ScriptDefinition scriptDef = (ScriptDefinition) toRun;
                        errors.addAll(processScriptDefinition(definition, loader, scriptDef));
                    }
                    else
                    {
                        log.warn(toRun.getClass().getName() + " is not a valid Harness or ScriptDefinition");
                    }

                    if (errors.size() > 0)
                    {
                        return errors;
                    }
                }
            }
            else
            {
                log.warn("No harnesses to run in script!");
            }
        }
        catch (Throwable e)
        {
            log.error("Unexpected error running script", e);
            errors.add(new HarnessError(definition.getPath().getAbsolutePath(), "Unknown Exception Running Script", e));
        }
        return errors;
    }

    protected List<HarnessError> processScriptDefinition(ScriptDefinition currentDefinition,
                                                         ScriptDefinitionLoader loader, ScriptDefinition toRun)
            throws Exception
    {
        List<HarnessError> errors = Collections.emptyList();
        log.info("Loading script " + toRun.getDir() + "/" + toRun.getFile());
        List<ScriptDefinition> scripts = loader.pullSubDefinitions(toRun, currentDefinition.getPath());
        Iterator<ScriptDefinition> definitions = scripts.iterator();
        while (definitions.hasNext() && (errors == null || errors.size() == 0))
        {
            ScriptDefinition subDefinition = definitions.next();
            ScriptRunner runner = PropertyObjectFactory.getInstance().buildRunner();
            if (runner != null)
            {
                errors = runner.run(subDefinition, context);
                log.trace("Resetting context harnesses");
            }
        }
        return errors;
    }

    public List getHarnesses()
    {
        return harnesses;
    }
}
