package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.WebRequest;
import junit.framework.TestCase;
import org.mash.config.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since Oct/3/17
 */
public class TestHttpClient extends TestCase
{
    public void testParamsAndHeaders() throws Exception {
        HttpClient client = new HttpClient(new StandardRequestFactory(), "GET");
        client.setAcceptType("application/json");

        Map<String, String> contents =  new HashMap<>();
        contents.put("someparam1", "somevalue1");
        List<Parameter> headers = new ArrayList<>();
        headers.add(new Parameter("header1", "value1"));
        client.initializeClient("http://www.google.com/search", contents, headers);

        WebRequest webRequest = client.getWebRequestSettings();
//        assertEquals("invalid body", webRequest.getRequestBody());
        //request parameters
        assertEquals("someparam1", webRequest.getRequestParameters().get(0).getName());
        //header parameters
        assertEquals("value1", webRequest.getAdditionalHeaders().get("header1"));
      }

    public void testParamsAndHeadersWithBody() throws Exception {
        HttpClient client = new HttpClient(new StandardRequestFactory(), "POST");
        client.setAcceptType("application/json");

        Map<String, String> contents =  new HashMap<>();
        contents.put("someparam1", "somevalue1");
        contents.put("body", "mybody");
        List<Parameter> headers = new ArrayList<>();
        headers.add(new Parameter("header1", "value1"));
        client.initializeClient("http://www.google.com/search", contents, headers);

        WebRequest webRequest = client.getWebRequestSettings();
//        assertEquals("invalid body", webRequest.getRequestBody());
        //request parameters
        assertTrue("parameters were incorrectly set with body", webRequest.getRequestParameters().size() == 0);
        assertEquals("mybody", webRequest.getRequestBody());
        //header parameters
        assertEquals("value1", webRequest.getAdditionalHeaders().get("header1"));
    }
}
