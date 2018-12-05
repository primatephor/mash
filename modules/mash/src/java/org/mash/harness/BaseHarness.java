package org.mash.harness;

import org.mash.config.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Hold parameters and configuration for harnesses
 */
public class BaseHarness implements Harness
{
    protected List<Configuration> configuration;
    protected List<Parameter> parameters;
    protected List<Attachment> attachments;
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

    public void setAttachments(List<Attachment> attachments) throws Exception
    {
        this.attachments = attachments;
    }

    public List<Attachment> getAttachments()
    {
        if (attachments == null)
        {
            attachments = new ArrayList<Attachment>();
        }
        return attachments;
    }

    public List<Parameter> getParameters()
    {
        if (parameters == null)
        {
            parameters = new ArrayList<>();
        }
        return parameters;
    }

    public List<Parameter> getParameters(String context)
    {
        List<Parameter> results = new ArrayList<>();
        for (Parameter parameter : getParameters())
        {
            if((context == null && parameter.getContext() == null) ||
               (context != null && context.equals(parameter.getContext())))
            {
                results.add(parameter);
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
