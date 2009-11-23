package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.SgmlPage;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * @author: teastlack
 * @since: Jul 4, 2009
 */
public class HttpClient
{
    private static final Logger LOG = Logger.getLogger(HttpClient.class.getName());

    private WebClient client;
    private WebRequestSettings webRequest;
    private SgmlPage webResponse;
    private WebRequestFactory factory;
    private String methodType;

    protected HttpClient(WebRequestFactory factory, String methodType)
    {
        this.factory = factory;
        this.methodType = methodType;
    }

    public void submit(String uri, Map<String, String> contents)
    {
        client = WebConversationHolder.getInstance();
        client.setJavaScriptEnabled(false);
        webRequest = factory.createRequest(methodType, uri, contents);

        try
        {
            webResponse = client.getPage(webRequest);
            LOG.debug("Received response");
        }
        catch (Exception e)
        {
            LOG.error("Error sending via HttpUnit to uri:" + uri, e);
        }
    }

    public WebClient getClient()
    {
        return client;
    }

    public WebRequestSettings getWebRequestSettings()
    {
        return webRequest;
    }

    public SgmlPage getWebResponse()
    {
        return webResponse;
    }
}
