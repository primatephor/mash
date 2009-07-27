package org.mash.harness.rest;

import junit.framework.TestCase;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.FrameSelector;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * @author: teastlack
 * @since: Jul 26, 2009
 */
public class TestRestResponse extends TestCase
{
    public void testXPath() throws MalformedURLException
    {
        RestWebResponse response = new RestWebResponse("<?xml version='1.0' encoding='utf-8'?>\n" +
                                                       "<SomeEntity>\n" +
                                                       "    <Firstname>first</Firstname>\n" +
                                                       "    <Lastname>last</Lastname>\n" +
                                                       "    <Address>\n" +
                                                       "        <Street1>delStreet1</Street1>\n" +
                                                       "        <Street2>delStreet2</Street2>\n" +
                                                       "    </Address>\n" +
                                                       "    <Value>1</Value>\n" +
                                                       "</SomeEntity>");
        RestResponse restResponse = new RestResponse(response);
        assertEquals("1", restResponse.getValue("/SomeEntity/Value"));
        assertEquals("first", restResponse.getValue("/SomeEntity/Firstname"));
    }

    private class RestWebResponse extends WebResponse
    {
        private String response;

        private RestWebResponse(String response) throws MalformedURLException
        {
            super(null, FrameSelector.TOP_FRAME, new URL("http://blah.blah.blah"));
            this.response = response;
        }

        public int getResponseCode()
        {
            return 200;
        }

        public String getResponseMessage()
        {
            return null;
        }

        public String[] getHeaderFieldNames()
        {
            return new String[0];
        }

        public String getHeaderField(String s)
        {
            return null;
        }

        public String toString()
        {
            return null;
        }

        public String[] getHeaderFields(String s)
        {
            return new String[0];
        }

        public String getText() throws IOException
        {
            return response;
        }
    }
}
