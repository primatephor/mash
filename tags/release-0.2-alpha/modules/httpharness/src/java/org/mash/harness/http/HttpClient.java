package org.mash.harness.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * @author: teastlack
 * @since: Jul 4, 2009
 */
public class HttpClient
{
    private static final Logger LOG = Logger.getLogger(HttpClient.class.getName());

    private WebConversation webConversation;
    private WebRequest webRequest;
    private WebResponse webResponse;
    private WebRequestFactory factory;
    private String methodType;

    protected HttpClient(WebRequestFactory factory, String methodType)
    {
        this.factory = factory;
        this.methodType = methodType;
    }

    public void submit(String uri, Map<String, String> contents)
    {
        webConversation = WebConversationHolder.getInstance();
        webRequest = factory.createRequest(methodType, uri, contents);

        try
        {
            webResponse = webConversation.getResponse(webRequest);
        }
        catch (Exception e)
        {
            LOG.error("Error sending via HttpUnit: " + e, e);
        }
    }

    public WebConversation getWebConversation()
    {
        return webConversation;
    }

    public WebRequest getWebRequest()
    {
        return webRequest;
    }

    public WebResponse getWebResponse()
    {
        return webResponse;
    }
}
