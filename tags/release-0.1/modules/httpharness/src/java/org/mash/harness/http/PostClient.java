package org.mash.harness.http;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;

import java.util.Map;
import java.util.Set;

/**
 * @author: teastlack
 * @since: Jul 4, 2009
 */
public class PostClient extends HttpClient
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
        PostMethodWebRequest webRequest = new PostMethodWebRequest(uri, true);
        populateRequestParameters(contents, webRequest);
        return webRequest;
    }

    private void populateRequestParameters(Map<String, String> contents, PostMethodWebRequest req)
    {
        Set<String> keys = contents.keySet();
        for (String key : keys)
        {
            req.setParameter(key, contents.get(key));
        }
    }
}
