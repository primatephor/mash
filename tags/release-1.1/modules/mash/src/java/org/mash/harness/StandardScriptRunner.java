package org.mash.harness;

import org.apache.log4j.Logger;
import org.mash.config.Configuration;
import org.mash.config.HarnessDefinition;
import org.mash.config.Parameter;
import org.mash.config.ScriptDefinition;
import org.mash.loader.HarnessBuilder;
import org.mash.loader.ScriptDefinitionLoader;
import org.mash.loader.harnesssetup.CalculatingConfigBuilder;
import org.mash.loader.harnesssetup.CalculatingParameterBuilder;
import org.mash.tool.ErrorFormatter;
import org.mash.tool.ErrorHandler;
import org.mash.tool.StringUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This runs through and invokes the harnesses.  Each test framework implementation (like JUnit) would call this piece
 * and behave appropriately based on the test errors.  Each harness could have errors, and will these errors will be
 * returned after a harness is run.
 * <p/>
 * The setup, run, verify, and teardown are invoked separately.  A runner could be extended to modify how these are
 * invoked here.
 * <p/>
 * User: teastlack Date: Jul 7, 2009 Time: 3:23:20 PM
 */
public class StandardScriptRunner implements ScriptRunner
{
    private static final Logger log = Logger.getLogger(StandardScriptRunner.class.getName());

    protected List harnesses;
    private List<SetupHarness> setupHarnesses;

    private RunHarness lastRun;
    private List<RunHarness> previousRuns;

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
        ScriptDefinition definition = new ScriptDefinitionLoader().pullFile(fileName, new File("."));

        if (definition != null)
        {
            ErrorHandler handler =
                    new ErrorHandler((ErrorFormatter) PropertyObjectFactory.getInstance().buildFormatter());
            handler.handleErrors(runner.run(definition), definition);
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
     * @param definition defined by the configurations
     * @return harnesses to be run
     * @throws Exception upon error
     */
    protected List buildHarnesses(ScriptDefinition definition) throws Exception
    {
        List results = new ArrayList();
        HarnessBuilder builder = new HarnessBuilder();
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
                    if (toAdd instanceof SetupHarness)
                    {
                        getSetupHarnesses().add((SetupHarness) toAdd);
                    }
                    results.add(toAdd);
                    log.trace("Done configuring " + harnessDefinition.getName());
                }
                else if (current instanceof ScriptDefinition)
                {
                    ScriptDefinition scriptDefinition = (ScriptDefinition) current;
                    ScriptDefinition toAdd = builder.buildScriptDefinition(scriptDefinition, definition.getPath());
                    log.info("Loading script " +
                            StringUtil.cleanNull(toAdd.getDir()) + "/" +
                            StringUtil.cleanNull(toAdd.getFile()));
                    results.add(toAdd);
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
    public List<HarnessError> run(ScriptDefinition definition) throws Exception
    {
        List<HarnessError> errors = new ArrayList<HarnessError>();
        try
        {
            this.harnesses = buildHarnesses(definition);
            ScriptDefinitionLoader loader = new ScriptDefinitionLoader();

            if (this.harnesses != null)
            {
                log.debug("Running test");
                CalculatingParameterBuilder parameterBuilder = new CalculatingParameterBuilder();
                CalculatingConfigBuilder configurationBuilder = new CalculatingConfigBuilder();

                for (Object toRun : this.harnesses)
                {
                    if (toRun instanceof Harness)
                    {
                        Harness harness = (Harness) toRun;
                        errors.addAll(processHarness(definition, configurationBuilder, parameterBuilder, harness));
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
            errors.add(new HarnessError("Running Script " + definition.getPath().getAbsolutePath() + " Failed",
                                        e.getMessage(), e));
        }
        return errors;
    }

    protected List<HarnessError> processScriptDefinition(ScriptDefinition definition,
                                                         ScriptDefinitionLoader loader,
                                                         ScriptDefinition scriptDef) throws Exception
    {
        List<HarnessError> errors = Collections.emptyList();
        log.info("Loading script " + scriptDef.getDir() + "/" + scriptDef.getFile());
        List<ScriptDefinition> scripts = loader.pullSubDefinitions(scriptDef, definition.getPath());
        for (ScriptDefinition subDefinition : scripts)
        {
            ScriptRunner runner = PropertyObjectFactory.getInstance().buildRunner();
            if (runner != null)
            {
                errors = runner.run(subDefinition);
                if (errors != null && errors.size() > 0)
                {
                    break;
                }
                else
                {
                    this.getPreviousRuns().addAll(runner.getPreviousRuns());
                    this.lastRun = runner.getLastRun();
                    this.getSetupHarnesses().addAll(runner.getSetupHarnesses());
                }
            }
        }
        return errors;
    }

    protected List<HarnessError> processHarness(ScriptDefinition definition,
                                                CalculatingConfigBuilder configurationBuilder,
                                                CalculatingParameterBuilder parameterBuilder,
                                                Harness harness) throws Exception
    {
        List<HarnessError> errors = Collections.emptyList();
        log.info("Running harness " + harness.getDefinition().getName());
        List<Configuration> configs = configurationBuilder.applyParameters(getPreviousRuns(), definition, harness.getDefinition());
        harness.setConfiguration(configs);
        List<Parameter> params = parameterBuilder.applyParameters(getPreviousRuns(), definition, harness.getDefinition());
        harness.setParameters(params);
        if (harness instanceof SetupHarness)
        {
            errors = runSetupHarness((SetupHarness) harness);
        }
        if (harness instanceof RunHarness)
        {
            errors = runRunHarness((RunHarness) harness);
        }
        if (harness instanceof VerifyHarness)
        {
            errors = runVerifyHarness((VerifyHarness) harness);
        }
        if (harness instanceof TeardownHarness)
        {
            errors = runTeardown((TeardownHarness) harness);
        }
        return errors;
    }

    protected List<HarnessError> runTeardown(TeardownHarness harness)
    {
        logMsg("teardown", harness);
        harness.teardown(getSetupHarnesses());
        return harness.getErrors();
    }

    protected List<HarnessError> runVerifyHarness(VerifyHarness harness)
    {
        logMsg("verify", harness);
        harness.verify(getLastRun(), getSetupHarnesses());
        return harness.getErrors();
    }

    protected List<HarnessError> runRunHarness(RunHarness harness)
    {
        logMsg("run", harness);
        lastRun = harness;
        lastRun.run(getPreviousRuns(), getSetupHarnesses());
        previousRuns.add(getLastRun());
        return harness.getErrors();
    }

    protected List<HarnessError> runSetupHarness(SetupHarness harness) throws Exception
    {
        logMsg("setup", harness);
        harness.setup();
        return harness.getErrors();
    }

    private void logMsg(String harnessType,
                        Harness harness)
    {
        String harnessName = harness.getClass().getName();
        if (harness.getDefinition().getName() != null)
        {
            harnessName = harness.getDefinition().getName();
        }
        log.info("Running " + harnessType + " '" + harnessName + "'");
    }

    public List getHarnesses()
    {
        return harnesses;
    }

    public List<RunHarness> getPreviousRuns()
    {
        if (previousRuns == null)
        {
            previousRuns = new ArrayList<RunHarness>();
        }
        return previousRuns;
    }

    public List<SetupHarness> getSetupHarnesses()
    {
        if (setupHarnesses == null)
        {
            setupHarnesses = new ArrayList<SetupHarness>();
        }
        return setupHarnesses;
    }

    public RunHarness getLastRun()
    {
        return lastRun;
    }
}
