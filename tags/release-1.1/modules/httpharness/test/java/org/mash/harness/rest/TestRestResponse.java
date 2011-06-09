package org.mash.harness.rest;

import junit.framework.TestCase;
import org.mash.config.Parameter;
import org.mash.harness.BaseHarness;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.harness.StandardVerifyHarness;
import org.mash.harness.XmlResponse;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

    public void testWhiteSpace() throws MalformedURLException
    {
        String response = "<ns4:Status xmlns:ns5=\"http://www.bitvault.com/schema/V1/definition\" xmlns:ns4=\"http://www.bitvault.com/schema/V1/status\" xmlns:ns3=\"http://www.bitvault.com/schema/V1/party\" xmlns:ns2=\"http://www.bitvault.com/schema/V1/init\">\n" +
                          "  <Current>\n" +
                          "    success\n" +
                          "  </Current>\n" +
                          "  <Date>\n" +
                          "    2009-12-08-08:00\n" +
                          "  </Date>\n" +
                          "  <Test>\n" +
                          "    haven't loaded this\n" +
                          "  </Test>\n" +
                          "</ns4:Status>";
        XmlResponse xmlResponse = new XmlResponse(response);

        StandardVerifyHarness verifyHarness = new StandardVerifyHarness();
        verifyHarness.getParameters().add(new Parameter("/Status/Current", "success"));
        verifyHarness.getParameters().add(new Parameter("/Status/Test", "haven't loaded this"));

        RunHarness harness = new MyRunHarness(xmlResponse);
        verifyHarness.verify(harness, null);

        assertEquals(0, verifyHarness.getErrors().size());
    }

    private class MyRunHarness extends BaseHarness implements RunHarness
    {
        private RunResponse response;

        private MyRunHarness(RunResponse response)
        {
            this.response = response;
        }

        public void run(List<RunHarness> previous, List<SetupHarness> setups)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public RunResponse getResponse()
        {
            return response;
        }
    }

}
