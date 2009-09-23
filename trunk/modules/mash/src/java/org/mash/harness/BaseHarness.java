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
        if (configuration == null)
        {
            configuration = new ArrayList<Configuration>();
        }
        return configuration;
    }

    public void setConfiguration(List<Configuration> configuration)
    {
        this.configuration = configuration;
    }

    public List<Parameter> getParameters()
    {
        if (parameters == null)
        {
            parameters = new ArrayList<Parameter>();
        }
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

    public String getConfigurationValue(String key)
    {
        String result = null;
        Configuration config = getConfiguration(key);
        if (config != null)
        {
            result = config.getValue();
        }
        return result;
    }

    public Configuration getConfiguration(String key)
    {
        Configuration result = null;
        for (Configuration configuration : getConfiguration())
        {
            if (configuration.getName() != null &&
                configuration.getName().equals(key))
            {
                result = configuration;
                break;
            }
        }
        return result;
    }

    public String getParameterValue(String key)
    {
        String result = null;
        Parameter param = getParameter(key);
        if (param != null)
        {
            result = param.getValue();
        }
        return result;
    }

    public Parameter getParameter(String key)
    {
        Parameter result = null;
        for (Parameter parameter : getParameters())
        {
            if (parameter.getName() != null &&
                parameter.getName().equals(key))
            {
                result = parameter;
                break;
            }
        }
        return result;
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

    public Boolean hasErrors()
    {
        return getErrors().size() > 0;
    }
}
