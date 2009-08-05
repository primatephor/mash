package org.mash.harness.http;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.PutMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

/**
 * Builds the standard POST, GET, PUT, DELETE Httpunit WebRequest object.  Will marshall any parameters submitted.
 *
 * @author: teastlack
 * @since: Jul 26, 2009
 */
public class StandardRequestFactory implements WebRequestFactory
{
    private static final Logger log = Logger.getLogger(StandardRequestFactory.class.getName());
    public static String BODY = "body";
    public static String CONTENT_TYPE = "content_type";

    public WebRequest createRequest(String methodType,
                                    String url,
                                    Map<String, String> contents)
    {
        WebRequest result = null;
        InputStream body = getInputStream(contents);
        String contentType = getContentType(contents);
        Method method = Method.valueOf(methodType);

        if (method != null)
        {
            if (Method.POST.equals(method))
            {
                if (body != null)
                {
                    result = new PostMethodWebRequest(url, body, contentType);
                }
                else
                {
                    result = new PostMethodWebRequest(url, true);
                }
            }
            if (Method.PUT.equals(method))
            {
                if (body != null)
                {
                    result = new PutMethodWebRequest(url, body, contentType);
                }
            }
            if (Method.GET.equals(method))
            {
                result = new GetMethodWebRequest(url);
            }
            if (Method.DELETE.equals(method))
            {
                result = new GetMethodWebRequest(url)
                {
                    public String getMethod()
                    {
                        return "DELETE";
                    }
                };
            }
            populateRequestParameters(contents, result);
        }
        return result;
    }

    protected void populateRequestParameters(Map<String, String> contents, WebRequest request)
    {
        Set<String> keys = contents.keySet();
        for (String key : keys)
        {
            if (!BODY.equals(key) && !CONTENT_TYPE.equals(key))
            {
                log.debug("Adding parameter '" + key + "' as:" + contents.get(key));
                request.setParameter(key, contents.get(key));
            }
        }
    }

    private String getContentType(Map<String, String> contents)
    {
        String result = "text/html";
        if (contents.get(CONTENT_TYPE) != null)
        {
            result = contents.get(CONTENT_TYPE);
        }
        return result;
    }

    private InputStream getInputStream(Map<String, String> contents)
    {
        InputStream result = null;
        if (contents.get(BODY) != null)
        {
            result = new ByteArrayInputStream(contents.get(BODY).getBytes());
        }
        return result;
    }

}
