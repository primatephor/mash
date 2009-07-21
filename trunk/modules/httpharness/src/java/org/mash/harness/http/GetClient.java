package org.mash.harness.http;

import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.GetMethodWebRequest;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;

/**
 * @author: teastlack
 * @since: Jul 4, 2009
 */
public class GetClient extends HttpClient
{
    /**
     * Build the web request for the particular method
     *
     * @param uri      to post to
     * @param contents map of parameters
     * @return the created and populated web request
     */
    protected WebRequest createWebRequest(String uri, Map<String, String> contents)
    {
        GetMethodWebRequest webRequest;
        String queryParams = encodeQueryParams(contents);
        webRequest = new GetMethodWebRequest(uri + "?" + queryParams);
        return webRequest;
    }

    private String encodeQueryParams(Map<String, String> contents)
    {
        StringBuffer query = new StringBuffer();
        Set<Map.Entry<String, String>> entries = contents.entrySet();
        Iterator<Map.Entry<String, String>> iter = entries.iterator();
        while (iter.hasNext())
        {
            Map.Entry<String, String> entry = iter.next();
            String value = encodeValue(entry.getValue());
            if (value != null)
            {
                query.append(entry.getKey()).append("=").append(value);
                if (iter.hasNext())
                {
                    query.append("&");
                }
            }
        }
        return query.toString();
    }

    private String encodeValue(String s)
    {
        String result = null;
        if (s != null)
        {
            result = s.replaceAll(" ", "+");
            result = result.replaceAll("!", "%21");
            result = result.replaceAll("\\*", "%2A");
            result = result.replaceAll("\"", "%22");
            result = result.replaceAll("'", "%27");
            result = result.replaceAll("\\(", "%28");
            result = result.replaceAll("\\)", "%29");
            result = result.replaceAll(";", "%3B");
            result = result.replaceAll(":", "%3A");
            result = result.replaceAll("@", "%40");
            result = result.replaceAll("&", "%26");
            result = result.replaceAll("=", "%3D");
            result = result.replaceAll("\\+", "%2B");
            result = result.replaceAll("$", "%24");
            result = result.replaceAll(",", "%2C");
            result = result.replaceAll("/", "%2F");
            result = result.replaceAll("\\?", "%3F");
            result = result.replaceAll("%", "%25");
            result = result.replaceAll("#", "%23");
            result = result.replaceAll("\\[", "%5B");
            result = result.replaceAll("\\]", "%5D");
        }
        return result;
    }
}
