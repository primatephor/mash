package org.mash.harness.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.config.Configuration;
import org.mash.harness.HarnessContext;
import org.mash.harness.RunResponse;
import org.mash.harness.http.HttpClient;
import org.mash.harness.http.HttpRunHarness;
import org.mash.harness.http.Method;
import org.mash.harness.http.StandardRequestFactory;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessName;

/**
 * Configurations:
 * <ul>
 * <li> 'clean' will create a new web conversation </li>
 * <li> 'url' is the resource url </li>
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
    private static final Logger LOG = LogManager.getLogger(RestRunHarness.class.getName());
    public static String DEFAULT_CONTENT_TYPE = "text/xml";
    private RunResponse xmlResponse;

    private String contentType = DEFAULT_CONTENT_TYPE;
    private String acceptType = DEFAULT_CONTENT_TYPE;

    public void run(HarnessContext context)
    {
        LOG.debug("Running restful harness");
        if (Method.POST.name().equalsIgnoreCase(type) ||
            Method.PUT.name().equalsIgnoreCase(type))
        {
            if (!hasParameter(StandardRequestFactory.BODY))
            {
                throw new IllegalArgumentException("'body' parameter is required for 'CREATE' or 'UPDATE' requests");
            }
        }
        super.run(context);
    }

    public RunResponse getResponse()
    {
        if (xmlResponse == null)
        {
            if (getPage() != null)
            {
                LOG.debug("Pulling rest response");
                xmlResponse = new RestResponse(getPage());
            }
        }
        return xmlResponse;
    }

    @Override
    protected HttpClient getClient(String clientType, String username, String password)
    {
        HttpClient client = new HttpClient(new StandardRequestFactory(), clientType, username, password);
        if (null != contentType)
        {
            client.setContentType(contentType);
        }
        if (null != acceptType)
        {
            client.setAcceptType(acceptType);
        }
        return client;
    }

    @HarnessConfiguration(name = "content_type")
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    @HarnessConfiguration(name = "accept_type")
    public void setAcceptType(String acceptType)
    {
        this.acceptType = acceptType;
    }
}
