/*
 * Copyright (c) 2011 Ensenda, Inc. All Rights Reserved.
 * This code  is the  sole  property  of  Ensenda Inc.,
 * and is protected  by  copyright  under the  laws of the United
 * States. This program is confidential, proprietary, and a trade
 * secret, not to be disclosed without written authorization from
 * Ensenda Inc.  Any  use, duplication, or  disclosure
 * of  this  program  by other than Ensenda Inc. and its
 * assigned licensees is strictly forbidden by law.
 */

package org.mash.harness;

import org.mash.config.ScriptDefinition;
import org.mash.config.Configuration;
import org.mash.config.Parameter;
import org.mash.loader.harnesssetup.AnnotatedHarness;
import org.mash.loader.harnesssetup.CalculatingParameterBuilder;
import org.mash.loader.harnesssetup.CalculatingConfigBuilder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.ArrayList;

/**
 * @author teastlack
 * @since Jun 13, 2011
 */
public class StandardHarnessRunner implements HarnessRunner
{
    private static final Logger log = Logger.getLogger(StandardHarnessRunner.class.getName());
    private CalculatingParameterBuilder parameterBuilder;
    private CalculatingConfigBuilder configurationBuilder;

    private List<SetupHarness> setupHarnesses;
    private RunHarness lastRun;
    private List<RunHarness> previousRuns;

    public StandardHarnessRunner()
    {
        parameterBuilder = new CalculatingParameterBuilder();
        configurationBuilder = new CalculatingConfigBuilder();
    }

    public List<HarnessError> run(ScriptDefinition definition, Harness harness, List<RunHarness> previousRuns)
    {
        List<HarnessError> errors = new ArrayList<HarnessError>();
        log.debug("Trying to run harness " + harness.getDefinition().getName());
        try
        {
            List<Configuration> configs =
                    configurationBuilder.applyParameters(previousRuns, definition, harness.getDefinition());
            harness.setConfiguration(configs);
            List<Parameter> params =
                    parameterBuilder.applyParameters(previousRuns, definition, harness.getDefinition());
            harness.setParameters(params);
            if (harness instanceof SetupHarness)
            {
                errors.addAll(runSetupHarness((SetupHarness) harness));
            }
            if (harness instanceof RunHarness)
            {
                errors.addAll(runRunHarness((RunHarness) harness));
            }
            if (harness instanceof VerifyHarness)
            {
                errors.addAll(runVerifyHarness((VerifyHarness) harness));
            }
            if (harness instanceof TeardownHarness)
            {
                errors.addAll(runTeardown((TeardownHarness) harness));
            }
        }
        catch (Exception e)
        {
            errors.add(new HarnessError(harness, "Exception Running Harness", e));
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
        getLastRun().run(getPreviousRuns(), getSetupHarnesses());
        getPreviousRuns().add(getLastRun());
        return harness.getErrors();
    }

    protected List<HarnessError> runSetupHarness(SetupHarness harness) throws Exception
    {
        logMsg("setup", harness);
        harness.setup();
        getSetupHarnesses().add(harness);
        return harness.getErrors();
    }

    private void logMsg(String harnessType, Harness harness)
    {
        StringBuilder message = new StringBuilder("Doing ");
        message.append(harnessType).append(" ");

        if (harness instanceof AnnotatedHarness)
        {
            AnnotatedHarness annotatedHarness = (AnnotatedHarness) harness;
            if (annotatedHarness.getWrap() != null)
            {
                message.append(annotatedHarness.getWrap().getClass().getName());
            }
        }
        else
        {
            message.append(harness.getClass().getName());
        }

        if (harness.getDefinition().getName() != null)
        {
            message.append(", name:");
            message.append(harness.getDefinition().getName());
        }
        log.info(message);
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
