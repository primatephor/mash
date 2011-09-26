package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import org.apache.commons.httpclient.Cookie;
import org.apache.log4j.Logger;
import org.mash.config.Parameter;
import org.mash.harness.*;
import org.mash.harness.rest.RestResponse;
import org.mash.loader.HarnessConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private String username;
    private String password;
    protected HttpClient client;
    protected RunResponse response;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        log.debug("Running Http Invocation");
        if (clean != null && Boolean.valueOf(clean))
        {
            WebConversationHolder.reset();
        }

        client = getClient(type, username, password);

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

    protected Set<Cookie> getCookies()
    {
        Set<Cookie> cookies = null;
        if (client != null)
        {
            WebClient webClient = client.getClient();
            if (null != webClient)
            {
                CookieManager cookieManager = webClient.getCookieManager();
                if (null != cookieManager)
                {
                    cookies = cookieManager.getCookies();
                }
            }
        }
        return cookies;
    }

    public RunResponse getResponse()
    {
        SgmlPage result = getSgmlPage();
        if (result != null)
        {
            if (result instanceof XmlPage)
            {
                response = new RestResponse(result);
            }
            else
            {
                response = new HttpResponse(result, getCookies());
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
    protected HttpClient getClient(String clientType, String username, String password)
    {
        return new HttpClient(new StandardRequestFactory(), clientType, username, password);
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

    @HarnessConfiguration(name = "username")
    public void setUsername(String username)
    {
        this.username = username;
    }

    @HarnessConfiguration(name = "password")
    public void setPassword(String password)
    {
        this.password = password;
    }
}
