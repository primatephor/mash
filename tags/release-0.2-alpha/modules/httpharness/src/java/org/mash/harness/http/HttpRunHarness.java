package org.mash.harness.http;

import org.apache.log4j.Logger;
import org.mash.config.Configuration;
import org.mash.config.Parameter;
import org.mash.harness.BaseHarness;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meterware.httpunit.WebResponse;

/**
 * Configurations:
 * <ul>
 * <li> 'clean' will create a new web conversation </li>
 * <li> 'url' is the url to submit to </li>
 * <li> 'type' is the type of web request ('POST' or 'GET')</li>
 * </ul>
 * <p/>
 * <p/>
 * Parameters are applied to the request type, and the request is invoked.  There are special parameters:
 * <ul>
 * <li> 'body' will be the streamed input.  If present, the request won't add this as a part of the parameter list, but
 * will instead submit this an an input stream </li>
 * <li> 'content_type' is the type of body.  This isn't required, default is 'text/html' </li>
 * </ul>
 *
 * @author: teastlack
 * @since: Jul 5, 2009
 */
public class HttpRunHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(HttpRunHarness.class.getName());
    private String url;
    private String type;
    private String clean;
    protected HttpClient client;
    protected RunResponse response;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        log.debug("Running");
        if (clean != null && Boolean.valueOf(clean))
        {
            WebConversationHolder.reset();
        }

        client = getClient(type);

        if (client != null)
        {
            Map<String, String> params = new HashMap<String, String>();
            for (Parameter parameter : getParameters())
            {
                params.put(parameter.getName(), parameter.getValue());
            }
            client.submit(url, params);
        }
    }

    protected WebResponse getWebResponse()
    {
        WebResponse result = null;
        if (client != null)
        {
            result = client.getWebResponse();
        }
        return result;
    }

    public RunResponse getResponse()
    {
        if (response == null)
        {
            response = new HttpResponse(getWebResponse());
        }
        return response;
    }

    public void setConfiguration(List<Configuration> configuration)
    {
        super.setConfiguration(configuration);
        for (Configuration config : configuration)
        {
            if ("url".equals(config.getName()))
            {
                url = config.getValue();
            }
            if ("type".equals(config.getName()))
            {
                type = config.getValue();
            }
            if ("clean".equals(config.getName()))
            {
                clean = config.getValue();
            }
        }
    }

    /**
     * Override this to create your on harness to extract the appropriate client type
     *
     * @param clientType (POST, PUT, DELETE, GET)
     * @return HttpClient
     */
    protected HttpClient getClient(String clientType)
    {
        return new HttpClient(new StandardRequestFactory(), clientType);
    }
}
