package org.mash.harness;

import org.mash.config.BaseParameter;
import org.mash.config.Configuration;
import org.mash.config.HarnessDefinition;
import org.mash.config.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Hold parameters and configuration for harnesses
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

    public List<Parameter> getParameters(String context)
    {
        List<Parameter> results = new ArrayList<Parameter>();
        if(context == null)
        {
            results = getParameters();
        }
        else
        {
            for (Parameter parameter : getParameters())
            {
                if(context.equals(parameter.getContext()))
                {
                    results.add(parameter);
                }
            }
        }
        return results;
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

    protected String getBaseParameterValue(String key, List<? extends BaseParameter> params)
    {
        String result = null;
        for (BaseParameter param: params)
        {
            if (param.getName() != null &&
                param.getName().equals(key))
            {
                result = param.getValue();
                break;
            }
        }
        return result;
    }

    public String getConfigurationValue(String key)
    {
        return getBaseParameterValue(key, getConfiguration());
    }

    public String getParameterValue(String key)
    {
        return getBaseParameterValue(key, getParameters());
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

    public boolean hasParameter(String key)
    {
        boolean result = false;
        for (Parameter parameter : getParameters())
        {
            if(parameter.getName() != null && parameter.getName().equals(key))
            {
                result = true;
                break;
            }
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

    public void addError(String message, Exception exception)
    {
        getErrors().add(new HarnessError(this, message, exception));
    }

    public void addError(String message, String description)
    {
        getErrors().add(new HarnessError(this, message, description));
    }
        
    public Boolean hasErrors()
    {
        return getErrors().size() > 0;
    }
}
