package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Builds the standard POST, GET, PUT, DELETE Httpunit WebRequest object.  Will marshall any parameters submitted.
 *
 * @author:
 * @since: Jul 26, 2009
 */
public class StandardRequestFactory implements WebRequestFactory
{
    private static final Logger log = Logger.getLogger(StandardRequestFactory.class.getName());
    public static String BODY = "body";

    public WebRequestSettings createRequest(String methodType, String url, Map<String, String> contents) throws Exception
    {
        WebRequestSettings settings = null;
        String body = null;
        if (contents.get(BODY) != null)
        {
            body = contents.get(BODY);
        }

        Method method = Method.GET;
        if (methodType != null)
        {
            method = Method.valueOf(methodType.toUpperCase());
        }

        if (method != null)
        {
            URL theUrl = new URI(url).toURL();
            HttpMethod httpMethod = method.getMethod();
            settings = new WebRequestSettings(theUrl, httpMethod);
            if (body != null)
            {
                settings.setRequestBody(body);
            }
            else
            {
                populateRequestParameters(contents, settings);
            }
        }
        return settings;
    }

    protected void populateRequestParameters(Map<String, String> contents, WebRequestSettings request)
    {
        Set<String> keys = contents.keySet();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : keys)
        {
            if (!BODY.equals(key))
            {
                log.info("Adding parameter '" + key + "' as:" + contents.get(key));
                NameValuePair pair = new NameValuePair();
                pair.setName(key);
                pair.setValue(contents.get(key));
                params.add(pair);
            }
        }

        if (params.size() > 0)
        {
            request.setRequestParameters(params);
        }
    }
}
