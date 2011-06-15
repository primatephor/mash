package org.mash.harness;

import junit.framework.TestCase;
import org.mash.tool.XmlAccessor;

/**
 *
 * @author teastlack
 * @since Oct 1, 2009 1:02:23 PM
 *
 */
public class TestXmlAccessor extends TestCase
{
    public void testXPath()
    {
        String xml = "<somexml>" +
                     "   <data1>first</data1>" +
                     "   <data2>" +
                     "       <data3>third</data3>" +
                     "   </data2>" +
                     "   <datalist>element1</datalist>" +
                     "   <datalist>element2</datalist>" +
                     "</somexml>";
        XmlAccessor xmlAccessor = new XmlAccessor(xml);
        assertEquals("first", xmlAccessor.getPath("/somexml/data1")[0]);
        assertEquals("element1", xmlAccessor.getPath("/somexml/datalist")[0]);
        assertEquals("element2", xmlAccessor.getPath("/somexml/datalist")[1]);
    }

    public void testSingleNamespace()
    {
        String xml = "<ns:somexml xmlns:ns=\"http://some.ns.com\" xmlns:ns2=\"http://some.ns2.com\">" +
                     " <ns2:data1>first</ns2:data1>" +
                     " <data2>" +
                     "  <data3>third</data3>" +
                     " </data2>" +
                     " <datalist>element1</datalist>" +
                     " <datalist>element2</datalist>" +
                     "</ns:somexml>";
        XmlAccessor xmlAccessor = new XmlAccessor(xml);
        assertEquals("first", xmlAccessor.getPath("/somexml/data1")[0]);
        assertEquals("element1", xmlAccessor.getPath("/somexml/datalist")[0]);
        assertEquals("element2", xmlAccessor.getPath("/somexml/datalist")[1]);
    }

    public void testEmbeddedNamespace()
    {
        String xml = "<somexml xmlns:ns=\"http://some.ns.com\" xmlns:ns2=\"http://some.ns2.com\">" +
                     " <ns2:data1>first</ns2:data1>" +
                     " <data2>" +
                     "  <data3>third</data3>" +
                     " </data2>" +
                     " <ns2:datalist>element1</ns2:datalist>" +
                     " <ns2:datalist>element2</ns2:datalist>" +
                     "</somexml>";
        XmlAccessor xmlAccessor = new XmlAccessor(xml);
        assertEquals("first", xmlAccessor.getPath("/somexml/data1")[0]);
        assertEquals("element1", xmlAccessor.getPath("/somexml/datalist")[0]);
        assertEquals("element2", xmlAccessor.getPath("/somexml/datalist")[1]);
    }

    public void testXmlTag()
    {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                     "<somexml>" +
                     "   <data1>first</data1>" +
                     "   <data2>" +
                     "       <data3>third</data3>" +
                     "   </data2>" +
                     "   <datalist>element1</datalist>" +
                     "   <datalist>element2</datalist>" +
                     "</somexml>"+
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                     "<somexml>" +
                     "   <data1>2first</data1>" +
                     "   <data2>" +
                     "       <data3>2third</data3>" +
                     "   </data2>" +
                     "   <datalist>2element1</datalist>" +
                     "   <datalist>2element2</datalist>" +
                     "</somexml>";
        XmlAccessor xmlAccessor = new XmlAccessor(xml);
        assertEquals("first", xmlAccessor.getPath("/somexml[1]/data1")[0]);
        assertEquals("2element1", xmlAccessor.getPath("/somexml[2]/datalist")[0]);
        assertEquals("2element2", xmlAccessor.getPath("/somexml[2]/datalist")[1]);

        String[] responses = xmlAccessor.getPath("//data3");
        assertEquals(2, responses.length);
        assertEquals("third", responses[0]);
        assertEquals("2third", responses[1]);
    }
}
