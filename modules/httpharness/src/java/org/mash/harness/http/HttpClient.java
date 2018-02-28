package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.config.Parameter;
import org.mash.tool.StringUtil;

import java.util.List;
import java.util.Map;

/**
 */
public class HttpClient
{
    private static final Logger log = LogManager.getLogger(HttpClient.class.getName());
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
        if(client != null || webRequest == null)
        {
            initializeClient(uri, contents, headers);
        }
        log.info("Invoking client for "+webRequest.getUrl().toString());
        webResponse = client.getPage(webRequest);
    }

    void initializeClient(String uri, Map<String, String> contents, List<Parameter> headers) throws Exception
    {
        client = WebConversationHolder.getInstance();
        client.getOptions().setJavaScriptEnabled(false);
        if (null != username && null != password)
        {
            DefaultCredentialsProvider credentials = new DefaultCredentialsProvider();
            credentials.addCredentials(username, password);
            client.setCredentialsProvider(credentials);
        }

        webRequest = factory.createRequest(methodType, uri, contents);
        modifyRequestHeader("Accept", acceptType);
        modifyRequestHeader("Content-Type", contentType);

        if(headers != null && headers.size() > 0)
        {
            for (Parameter header : headers)
            {
                modifyRequestHeader(header.getName(), header.getValue());
            }
        }
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
    }

    private void modifyRequestHeader(String name, String value) {
        if(!StringUtil.isEmpty(value))
        {
            log.trace("Setting request header "+name+" to "+value);
            webRequest.setAdditionalHeader(name, value);
        }
        else
        {
            log.trace("Removing request header "+name);
            webRequest.removeAdditionalHeader(name);
        }
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setAcceptType(String acceptType) {
        this.acceptType = acceptType;
    }

    WebClient getClient()
    {
        return client;
    }

    WebRequest getWebRequestSettings()
    {
        return webRequest;
    }

    Page getWebResponse()
    {
        return webResponse;
    }
}
