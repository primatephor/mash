package org.mash.harness.http;

import com.meterware.httpunit.WebResponse;
import org.apache.log4j.Logger;
import org.mash.config.Parameter;
import org.mash.harness.BaseHarness;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configurations:
 * <ul>
 * <li> 'clean' will create a new web conversation </li>
 * <li> 'url' is the url to submit to </li>
 * <li> 'type' is the type of web request ('POST' or 'GET')</li>
 * <li> 'content_type' is the type of body.  This isn't required, default is 'text/html' </li>
 * </ul>
 * <p/>
 * <p/>
 * Parameters are applied to the request type, and the request is invoked.  There are special parameters:
 * <ul>
 * <li> 'body' will be the streamed input.  If present, the request won't add this as a part of the parameter list, but
 * will instead submit this an an input stream </li>
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
    private String contentType;
    private String clean;
    protected HttpClient client;
    protected RunResponse response;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        log.debug("Running Http Invocation");
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

            if (contentType != null)
            {
                params.put(StandardRequestFactory.CONTENT_TYPE, contentType);
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

    @HarnessConfiguration(name = "url")
    public void setUrl(String url)
    {
        this.url = url;
    }

    @HarnessConfiguration(name = "type")
    public void setType(String type)
    {
        this.type = type;
    }

    @HarnessConfiguration(name = "content_type")
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    @HarnessConfiguration(name = "clean")
    public void setClean(String clean)
    {
        this.clean = clean;
    }
}
