package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;

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
    private String username;
    private String password;
    private String contentType;

    public HttpClient(WebRequestFactory factory, String methodType, String username, String password, String contentType)
    {
        this.factory = factory;
        this.methodType = methodType;
        this.username = username;
        this.password = password;
        this.contentType = contentType;
    }

    public HttpClient(WebRequestFactory factory, String methodType, String username, String password)
    {
        this.factory = factory;
        this.methodType = methodType;
        this.username = username;
        this.password = password;
    }

    public HttpClient(WebRequestFactory factory, String methodType)
    {
        this(factory, methodType, null, null);
    }

    public HttpClient(WebRequestFactory factory)
    {
        this(factory, null);
    }

    public void submit(String uri, Map<String, String> contents) throws Exception
    {
        client = WebConversationHolder.getInstance();
        client.setJavaScriptEnabled(false);
        if (null != username && null != password)
        {
            DefaultCredentialsProvider credentials = new DefaultCredentialsProvider();
            credentials.addCredentials(username, password);
            client.setCredentialsProvider(credentials);
        }
        if (null != contentType)
        {
            client.addRequestHeader("Accept", contentType);
            client.addRequestHeader("Content-Type", contentType);
        }
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
