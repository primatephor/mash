package org.mash.harness;

import org.apache.log4j.Logger;
import org.mash.config.Configuration;
import org.mash.config.HarnessDefinition;
import org.mash.config.Parameter;
import org.mash.config.ScriptDefinition;
import org.mash.loader.ConfigurationBuilder;
import org.mash.loader.HarnessBuilder;
import org.mash.loader.ParameterBuilder;
import org.mash.loader.ScriptDefinitionLoader;
import org.mash.loader.harnesssetup.CalculatingConfigBuilder;
import org.mash.loader.harnesssetup.CalculatingParameterBuilder;

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
    private List<RunHarness> previousRun = new ArrayList<RunHarness>();

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
            setupHarnesses = new ArrayList<SetupHarness>();
            for (Object current : definition.getHarnesses())
            {
                if (current instanceof HarnessDefinition)
                {
                    HarnessDefinition harnessDefinition = (HarnessDefinition) current;
                    harnessDefinition.setScriptDefinition(definition);
                    Harness toAdd = builder.buildHarness(harnessDefinition);
                    ConfigurationBuilder configurationBuilder = new CalculatingConfigBuilder();
                    List<Configuration> configs = configurationBuilder.applyConfiguration(definition, toAdd);
                    toAdd.setConfiguration(configs);
                    results.add(toAdd);
                    if (toAdd instanceof SetupHarness)
                    {
                        setupHarnesses.add((SetupHarness) toAdd);
                    }
                }

                if (current instanceof ScriptDefinition)
                {
                    ScriptDefinition scriptDefinition = (ScriptDefinition) current;
                    ScriptDefinition toAdd = builder.buildScriptDefinition(scriptDefinition, definition.getPath());
                    results.add(toAdd);
                }
            }
        }
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
        this.harnesses = buildHarnesses(definition);
        ScriptDefinitionLoader loader = new ScriptDefinitionLoader();

        if (this.harnesses != null)
        {
            log.debug("Running test");
            ParameterBuilder parameterBuilder = new CalculatingParameterBuilder();
            for (Object toRun : this.harnesses)
            {
                List<HarnessError> errors = Collections.emptyList();
                if (toRun instanceof Harness)
                {
                    Harness harness = (Harness) toRun;
                    List<Parameter> params = parameterBuilder.applyParameters(previousRun, definition, harness.getDefinition());
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
                }

                if (toRun instanceof ScriptDefinition)
                {
                    List<ScriptDefinition> scripts = loader.pullSubDefinitions((ScriptDefinition) toRun,
                                                                               definition.getPath());
                    for (ScriptDefinition subDefinition : scripts)
                    {
                        ScriptRunner runner = RunnerFactory.getInstance().buildRunner();
                        if (runner != null)
                        {
                            errors = runner.run(subDefinition);
                        }

                        if (errors != null && errors.size() > 0)
                        {
                            break;
                        }
                    }
                }

                if (errors != null && errors.size() > 0)
                {
                    return errors;
                }
            }
        }
        else
        {
            log.debug("No harnesses to run");
        }
        return Collections.emptyList();
    }

    protected List<HarnessError> runTeardown(TeardownHarness harness)
    {
        logMsg("teardown", harness);
        harness.teardown(setupHarnesses);
        return harness.getErrors();
    }

    protected List<HarnessError> runVerifyHarness(VerifyHarness harness)
    {
        logMsg("verify", harness);
        harness.verify(lastRun, setupHarnesses);
        return harness.getErrors();
    }

    protected List<HarnessError> runRunHarness(RunHarness harness)
    {
        logMsg("run", harness);
        lastRun = harness;
        lastRun.run(previousRun, setupHarnesses);
        previousRun.add(lastRun);
        return harness.getErrors();
    }

    protected List<HarnessError> runSetupHarness(SetupHarness harness) throws Exception
    {
        logMsg("setup", harness);
        harness.setup();
        return harness.getErrors();
    }

    private void logMsg(String harnessType, Harness harness)
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
}
