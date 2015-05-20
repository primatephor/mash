package org.mash.harness;

import org.mash.config.Configuration;
import org.mash.config.Parameter;
import org.mash.loader.HarnessName;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Date: Jul 1, 2009 Time: 6:36:21 PM
 */
@HarnessName(name = "http")
public class HttpRunHarness extends BaseHarness implements RunHarness
{
    private List<Configuration> configs;
    private List<Parameter> params;
    public Boolean runCalled = false;
    Map<String, String> testResponse = new HashMap<String, String>();
    public static int callCount = 0;

    public HttpRunHarness()
    {
    }

    public void run(HarnessContext context)
    {
        runCalled = true;
        if (this.getDefinition() != null &&
            this.getDefinition().getName() != null &&
            this.getDefinition().getName().equals("login"))
        {
            this.testResponse.put("session", "login_session");
        }
        this.testResponse.put("someparam", "somevalue");
        callCount++;
    }

    public static void reset()
    {
        callCount = 0;
    }

    public RunResponse getResponse()
    {
        return new MyResponse();
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

    public Map<String, String> getTestResponse()
    {
        return testResponse;
    }

    public void setTestResponse(Map<String, String> testResponse)
    {
        this.testResponse = testResponse;
    }

    private class MyResponse implements RunResponse
    {
        public String getValue(String name)
        {
            return testResponse.get(name);
        }

        public Collection<String> getValues(String name)
        {
            return testResponse.values();
        }

        public String getString()
        {
            return "THIS IS A TEST";
        }

        public Collection<String> getValues()
        {
            return testResponse.values();
        }
    }
}
