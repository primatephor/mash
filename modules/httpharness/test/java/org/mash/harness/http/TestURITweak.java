package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.WebRequest;
import junit.framework.TestCase;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author teastlack
 * @since 4/25/17 1:12 PM
 */
public class TestURITweak extends TestCase
{
    public void testQuery() throws URISyntaxException
    {
        URI myUri = new URI("myhost.com/test?thisParam=thisValue");
        assertEquals("thisParam=thisValue", myUri.getQuery());
        assertEquals("myhost.com/test?thisParam=thisValue", myUri.toString());
    }

    public void testRequest() throws Exception
    {
        StandardRequestFactory requestFactory = new StandardRequestFactory();
        WebRequest webRequest= requestFactory.createRequest(null, "http://www.myhost.com/test?thisParam=thisValue", null);
        assertEquals("http://www.myhost.com/test?thisParam=thisValue", webRequest.getUrl().toString());
    }
}
