package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.*;
import org.apache.log4j.Logger;
import org.mash.config.Parameter;

import java.util.List;
import java.util.Map;

/**
 */
public class HttpClient
{
    private static final Logger log = Logger.getLogger(HttpClient.class.getName());

    private WebClient client;
    private WebRequest webRequest;
    private Page webResponse;
    private WebRequestFactory factory;
    private String methodType;
    private String username;
    private String password;
    private String contentType;
    private String acceptType;

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

    public void submit(String uri, Map<String, String> contents, List<Parameter> headers) throws Exception
    {
        webResponse = getPage(uri, contents, headers);
    }

    public Page getPage(String uri, Map<String, String> contents, List<Parameter> headers) throws Exception
    {
        initializeClient(uri, contents, headers);
        log.info("Invoking client for "+webRequest.getUrl().toString());
        return client.getPage(webRequest);
    }

    private void initializeClient(String uri, Map<String, String> contents, List<Parameter> headers) throws Exception
    {
        client = WebConversationHolder.getInstance();
        client.getOptions().setJavaScriptEnabled(false);
        if (null != username && null != password)
        {
            DefaultCredentialsProvider credentials = new DefaultCredentialsProvider();
            credentials.addCredentials(username, password);
            client.setCredentialsProvider(credentials);
        }

        modifyRequestHeader("Accept", acceptType);
        modifyRequestHeader("Content-Type", contentType);
        webRequest = factory.createRequest(methodType, uri, contents);

        if(headers != null && headers.size() > 0)
        {
            for (Parameter header : headers)
            {
                webRequest.setAdditionalHeader(header.getName(), header.getValue());
                //client.addRequestHeader(header.getName(), header.getValue());
            }
        }
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
    }

    private void modifyRequestHeader(String name, String value) {
        if(value != null)
        {
            log.trace("Setting request header "+name+" to "+value);
            client.addRequestHeader(name, value);
        }
        else
        {
            log.trace("Removing request header "+name);
            client.removeRequestHeader(name);
        }
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setAcceptType(String acceptType) {
        this.acceptType = acceptType;
    }

    public WebClient getClient()
    {
        return client;
    }

    public WebRequest getWebRequestSettings()
    {
        return webRequest;
    }

    public Page getWebResponse()
    {
        return webResponse;
    }
}
