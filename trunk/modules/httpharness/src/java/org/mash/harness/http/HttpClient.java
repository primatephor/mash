package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.SgmlPage;

import java.util.Map;

/**
 * @author: teastlack
 * @since: Jul 4, 2009
 */
public class HttpClient
{
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

    public void submit(String uri, Map<String, String> contents) throws Exception
    {
        client = WebConversationHolder.getInstance();
        client.setJavaScriptEnabled(false);
        webRequest = factory.createRequest(methodType, uri, contents);
        client.setThrowExceptionOnFailingStatusCode(false);
        webResponse = client.getPage(webRequest);
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
