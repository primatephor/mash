package org.mash.loader.harnesssetup;

import org.apache.log4j.Logger;
import org.mash.config.BaseParameter;
import org.mash.config.Configuration;
import org.mash.config.HarnessDefinition;
import org.mash.config.Parameter;
import org.mash.harness.Harness;
import org.mash.harness.HarnessError;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author teastlack
 * @since Sep 17, 2009 1:34:00 PM
 *
 */
public class AnnotatedHarness implements Harness
{
    private static final Logger log = Logger.getLogger(AnnotatedHarness.class.getName());
    private Harness wrap;
    private Map<String, Method> paramSetters;
    private Map<String, Method> configSetters;

    public AnnotatedHarness(Harness wrap)
    {
        this.wrap = wrap;
        Method[] methods = wrap.getClass().getMethods();
        paramSetters = new HashMap<String, Method>();
        configSetters = new HashMap<String, Method>();
        for (Method method : methods)
        {
            HarnessConfiguration config = method.getAnnotation(HarnessConfiguration.class);
            if (config != null)
            {
                configSetters.put(config.name(), method);
            }
            HarnessParameter param = method.getAnnotation(HarnessParameter.class);
            if (param != null)
            {
                paramSetters.put(param.name(), method);
            }
        }
    }

    public void setConfiguration(List<Configuration> configs)
    {
        this.wrap.setConfiguration(configs);
        if (configs != null)
        {
            for (Configuration config : configs)
            {
                invokeSetter(configSetters, config);
            }
        }
    }

    private void invokeSetter(Map<String, Method> setMethods, BaseParameter param)
    {
        if (setMethods.get(param.getName()) != null)
        {
            Method method = setMethods.get(param.getName());
            try
            {
                method.invoke(wrap, param.getValue());
            }
            catch (IllegalAccessException e)
            {
                log.error("Unexpected error accessing method " + method.getName() + " for setting " + param.getName(), e);
            }
            catch (InvocationTargetException e)
            {
                log.error("Unexpected error invoking method " + method.getName() + " for setting " + param.getName(), e);
            }
        }
    }

    public void setParameters(List<Parameter> params)
    {
        this.wrap.setParameters(params);
        if (params != null)
        {
            for (BaseParameter param : params)
            {
                invokeSetter(paramSetters, param);
            }
        }
    }

    public List<Configuration> getConfiguration()
    {
        return wrap.getConfiguration();
    }

    public List<Parameter> getParameters()
    {
        return wrap.getParameters();
    }

    public HarnessDefinition getDefinition()
    {
        return wrap.getDefinition();
    }

    public void setDefinition(HarnessDefinition harnessDefinition)
    {
        wrap.setDefinition(harnessDefinition);
    }

    public List<HarnessError> getErrors()
    {
        return wrap.getErrors();
    }

    public Harness getWrap()
    {
        return wrap;
    }
}