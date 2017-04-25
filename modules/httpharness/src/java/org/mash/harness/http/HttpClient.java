package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.*;
import org.apache.log4j.Logger;
import org.mash.config.Parameter;

import java.util.List;
import java.util.Map;

/**
 * @author:
 * @since: Jul 4, 2009
 */
public class HttpClient
{
    private static final Logger log = Logger.getLogger(HttpClient.class.getName());

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

    public void submit(String uri, Map<String, String> contents, List<Parameter> headers) throws Exception
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
            log.trace("Setting content type to " + contentType);
            client.addRequestHeader("Accept", contentType);
            client.addRequestHeader("Content-Type", contentType);
        }
        else
        {
            log.trace("Using default content type");
            client.removeRequestHeader("Accept");
            client.removeRequestHeader("Content-Type");
        }
        if(headers != null && headers.size() > 0)
        {
            for (Parameter header : headers)
            {
                client.addRequestHeader(header.getName(), header.getValue());
            }
        }
        webRequest = factory.createRequest(methodType, uri, contents);
        client.setThrowExceptionOnFailingStatusCode(false);
        log.info("Invoking client for "+webRequest.getUrl().toString());
        Page page = client.getPage(webRequest);
        if (page instanceof SgmlPage)
        {
            webResponse = (SgmlPage) page;
        }
        else
        {
            log.warn("Unexpected response: " + page.getWebResponse().getContentAsString());
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
