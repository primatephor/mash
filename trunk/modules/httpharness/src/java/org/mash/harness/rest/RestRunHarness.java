package org.mash.harness.rest;

import org.apache.log4j.Logger;
import org.mash.config.Configuration;
import org.mash.config.Parameter;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.harness.http.HttpClient;
import org.mash.harness.http.HttpRunHarness;
import org.mash.harness.http.Method;
import org.mash.harness.http.StandardRequestFactory;
import org.mash.loader.HarnessName;

import java.util.List;

/**
 * Configurations:
 * <ul>
 * <li> 'clean' will create a new web conversation </li>
 * <li> 'url' is the resource url </li>
 * <li> 'verb' is the type of rest request ('CREATE', 'READ', 'UPDATE', 'DELETE').  REQUIRED</li>
 * </ul>
 * <p/>
 * <p/>
 * Parameters are applied to the request type, and the request is invoked.  There are special parameters:
 * <ul>
 * <li> 'body' will be the streamed input.  This is required if not a 'read' or 'delete' as it's the resource in
 * question </li>
 * <li> 'content_type' is the type of body.  This isn't required, default is 'text/xml' </li>
 * </ul>
 */
@HarnessName(name = "rest")
public class RestRunHarness extends HttpRunHarness
{
    private static final Logger LOG = Logger.getLogger(RestRunHarness.class.getName());
    public static String DEFAULT_CONTENT_TYPE = "text/xml";
    private RunResponse xmlResponse;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        LOG.debug("Running restful harness");
        String type = getConfigurationValue("type");
        if (Method.POST.name().equalsIgnoreCase(type) ||
            Method.PUT.name().equalsIgnoreCase(type))
        {
            Parameter param = getParameter(StandardRequestFactory.BODY);
            if (param == null)
            {
                throw new IllegalArgumentException("'body' parameter is required for 'CREATE' or 'UPDATE' requests");
            }
        }
        super.run(previous, setups);
    }

    public RunResponse getResponse()
    {
        if (xmlResponse == null)
        {
            if (getSgmlPage() != null)
            {
                LOG.debug("Pulling sgml response");
                xmlResponse = new RestResponse(getSgmlPage());
                if (LOG.isTraceEnabled())
                {
                    LOG.debug("Response:" + xmlResponse.getString());
                }
            }
        }
        return xmlResponse;
    }

    @Override
    protected HttpClient getClient(String clientType, String username, String password)
    {
        String contentType = getParameterValue("content_type");
        if (null == contentType)
        {
            contentType = DEFAULT_CONTENT_TYPE;
        }
        return new HttpClient(new StandardRequestFactory(), clientType, username, password, contentType);
    }

    /**
     * Replace the CRUD verb specification with the appropriate Http type.
     *
     * @param configuration list of configs, including type
     */
    public void setConfiguration(List<Configuration> configuration)
    {
        boolean typePresent = false;
        for (Configuration config : configuration)
        {
            if ("verb".equals(config.getName()))
            {
                String type = config.getValue();
                Verb verb = Verb.valueOf(type);
                config.setValue(verb.getMethod().name());
                config.setName("type");
                typePresent = true;
            }
        }

        if (!typePresent)
        {
            throw new IllegalArgumentException("'verb' configuration MUST be present");
        }
        super.setConfiguration(configuration);
    }
}
