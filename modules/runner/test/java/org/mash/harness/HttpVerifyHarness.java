package org.mash.harness;

import org.mash.config.Configuration;
import org.mash.config.Parameter;

import java.util.List;

/**
 *  Date: Jul 1, 2009 Time: 6:38:00 PM
 */
public class HttpVerifyHarness extends BaseHarness implements VerifyHarness
{
    private List<Configuration> configs;
    private List<Parameter> params;
    public Boolean verifyCalled = false;

    public void verify(RunHarness run, List<SetupHarness> setup)
    {
        verifyCalled = true;
    }

    public void setConfiguration(List<Configuration> configs)
    {
        this.configs = configs;
    }

    public void setParameters(List<Parameter> params)
    {
        this.params = params;
    }

    public List<Configuration> getConfigs()
    {
        return configs;
    }

    public List<Parameter> getParams()
    {
        return params;
    }
}