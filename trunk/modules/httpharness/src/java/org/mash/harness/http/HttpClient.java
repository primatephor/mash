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
public abstract class HttpClient
{
    private static final Logger LOG = Logger.getLogger(HttpClient.class.getName());

    private WebConversation webConversation;
    private WebRequest webRequest;
    private WebResponse webResponse;

    public void submit(String uri, Map<String, String> contents)
    {
        webConversation = WebConversationHolder.getInstance();
        webRequest = createWebRequest(uri, contents);
        try
        {
            webResponse = webConversation.getResponse(webRequest);
        }
        catch (Exception e)
        {
            LOG.error("Error sending via HttpUnit: " + e, e);
        }
    }

    /**
     * Build the web request for the particular method
     *
     * @param uri      to post to
     * @param contents map of parameters
     * @return the created and populated web request
     */
    protected abstract WebRequest createWebRequest(String uri, Map<String, String> contents);


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
