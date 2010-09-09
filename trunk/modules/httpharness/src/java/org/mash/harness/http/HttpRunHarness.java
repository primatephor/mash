package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import org.apache.log4j.Logger;
import org.mash.config.Parameter;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.harness.rest.RestResponse;
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
 * </ul>
 * <p/>
 * <p/>
 * Parameters are applied to the request type, and the request is invoked.  There are special parameters:
 * <ul>
 * <li> 'body' will be the streamed input.  If present, the request won't add this as a part of the parameter list, but
 * will instead submit this as an input stream </li>
 * </ul>
 *
 * @author teastlack
 * @since Jul 5, 2009
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

            try
            {
                client.submit(url, params);
                if (log.isTraceEnabled())
                {
                    RunResponse response = getResponse();
                    if (response != null)
                    {
                        log.trace("Response:" + response.getString());
                    }
                }
            }
            catch (Exception e)
            {
                log.error("Unexpected error sending to " + url, e);
                this.getErrors().add(new HarnessError(this.getName(), "Unexpected error sending to " + url, e));
            }
        }
    }

    protected SgmlPage getSgmlPage()
    {
        SgmlPage result = null;
        if (client != null)
        {
            result = client.getWebResponse();
        }
        else
        {
            log.warn("Client is null!");
        }
        return result;
    }

    public RunResponse getResponse()
    {
        SgmlPage result = getSgmlPage();
        if (result != null)
        {
            if (result instanceof XmlPage)
            {
                response = new RestResponse((XmlPage) result);
            }
            else
            {
                response = new HttpResponse(result);
            }
        }
        else
        {
            log.warn("No response!");
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

    @HarnessConfiguration(name = "clean")
    public void setClean(String clean)
    {
        this.clean = clean;
    }
}
