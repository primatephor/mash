package org.mash.harness.http;

import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PutMethodWebRequest;

import java.util.Map;
import java.util.Set;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

/**
 * @author: teastlack
 * @since: Jul 26, 2009
 */
public class StandardRequestFactory implements WebRequestFactory
{
    private static String BODY = "body";
    private static String CONTENT_TYPE = "content_type";

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
                result = new GetMethodWebRequest(url){
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
            request.setParameter(key, contents.get(key));
        }
    }

    private String getContentType(Map<String, String> contents)
    {
        String result = "text/xml";
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
