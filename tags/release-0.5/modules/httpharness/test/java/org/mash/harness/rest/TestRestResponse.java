package org.mash.harness.rest;

import junit.framework.TestCase;
import org.mash.harness.XmlResponse;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author: teastlack
 * @since: Jul 26, 2009
 */
public class TestRestResponse extends TestCase
{
    public void testXPath() throws MalformedURLException
    {
        String response = "<?xml version='1.0' encoding='utf-8'?>\n" +
                          "<SomeEntity>\n" +
                          "    <Firstname>first</Firstname>\n" +
                          "    <Lastname>last</Lastname>\n" +
                          "    <Address>\n" +
                          "        <Street1>delStreet1</Street1>\n" +
                          "        <Street2>delStreet2</Street2>\n" +
                          "    </Address>\n" +
                          "    <Value>1</Value>\n" +
                          "</SomeEntity>";
        XmlResponse xmlResponse = new XmlResponse(response);
        assertEquals("1", xmlResponse.getValue("/SomeEntity/Value"));
        assertEquals("first", xmlResponse.getValue("/SomeEntity/Firstname"));
    }

    public void testXPathList() throws MalformedURLException
    {
        String response = "<?xml version='1.0' encoding='utf-8'?>\n" +
                          "<SomeEntity>\n" +
                          "    <Firstname>first</Firstname>\n" +
                          "    <Lastname>last</Lastname>\n" +
                          "    <Address>\n" +
                          "        <Street1>delStreet1</Street1>\n" +
                          "        <Street2>delStreet2</Street2>\n" +
                          "    </Address>\n" +
                          "    <Address>\n" +
                          "        <Street1>delStreet3</Street1>\n" +
                          "        <Street2>delStreet4</Street2>\n" +
                          "    </Address>\n" +
                          "    <Value>1</Value>\n" +
                          "</SomeEntity>";
        XmlResponse xmlResponse = new XmlResponse(response);
        assertEquals("first", xmlResponse.getValue("/SomeEntity/Firstname"));
        Collection<String> responses = xmlResponse.getValues("//Street1");
        assertEquals(2, responses.size());
        Iterator<String> responseIter = responses.iterator();
        assertEquals("delStreet1", responseIter.next());
        assertEquals("delStreet3", responseIter.next());
    }
}
