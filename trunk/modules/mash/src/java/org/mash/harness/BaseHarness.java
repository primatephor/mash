package org.mash.harness;

import org.mash.config.Configuration;
import org.mash.config.HarnessDefinition;
import org.mash.config.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: teastlack
 * @since: Jul 5, 2009
 */
public class BaseHarness implements Harness
{
    protected List<Configuration> configuration;
    protected List<Parameter> parameters;
    protected HarnessDefinition definition;
    private List<HarnessError> harnessErrors;

    public List<Configuration> getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration(List<Configuration> configuration)
    {
        this.configuration = configuration;
    }

    public List<Parameter> getParameters()
    {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters)
    {
        this.parameters = parameters;
    }

    public HarnessDefinition getDefinition()
    {
        return definition;
    }

    public void setDefinition(HarnessDefinition definition)
    {
        this.definition = definition;
    }

    public String getName()
    {
        String result = this.getClass().getName();
        if (this.getDefinition() != null &&
            this.getDefinition().getName() != null)
        {
            result = this.getDefinition().getName();
        }
        return result;
    }

    public List<HarnessError> getErrors()
    {
        if (harnessErrors == null)
        {
            harnessErrors = new ArrayList<HarnessError>();
        }
        return harnessErrors;
    }
}
