package org.mash.tool;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;

/**
 *
 * @author teastlack
 * @since Sep 17, 2009 6:07:28 PM
 *
 */
public class XmlAccessor
{
    private static final Logger log = Logger.getLogger(XmlAccessor.class.getName());
    private String xml;
    private Document document;

    public XmlAccessor(String xml)
    {
        this.xml = xml;
    }

    public Document getDocument()
    {
        if (document == null)
        {
            ByteArrayInputStream inputStream = null;
            xml = removeNamespaces(xml);
            try
            {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                docFactory.setNamespaceAware(true);
                DocumentBuilder builder = docFactory.newDocumentBuilder();
                inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                document = builder.parse(inputStream);
            }
            catch (Exception e)
            {
                log.info("Failed to parse xml", e);
            }
            finally
            {
                close(inputStream);
            }
        }
        return document;
    }


    public String[] getPath(String path)
    {
        String[] result = null;

        if (getDocument() != null &&
            path != null)
        {
            try
            {
                log.debug("Searching for " + path);
                XPathFactory xpFactory = XPathFactory.newInstance();
                XPath xpath = xpFactory.newXPath();
                XPathExpression expression = xpath.compile(path);
                NodeList nodes = (NodeList) expression.evaluate(getDocument(), XPathConstants.NODESET);

                result = new String[nodes.getLength()];
                for (int index = 0; index < result.length; index++)
                {
                    result[index] = nodes.item(index).getTextContent();
                }
            }
            catch (Exception e)
            {
                log.info("Failed to parse xml", e);
            }
        }
        return result;
    }

    public String getPath(String path, String delimiter)
    {
        String[] results = getPath(path);
        if (null == results)
        {
            return null;
        }
        if (results.length == 0)
        {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(results[0]);
        for (int index = 1; index < results.length; index++)
        {
            builder.append(delimiter).append(results[index]);
        }
        return builder.toString();
    }

    private void close(Closeable c)
    {
        try
        {
            if (c != null)
            {
                c.close();
            }
        }
        catch (IOException e)
        {
            log.error("Error when closing IOStream", e);
        }
    }

    private String removeNamespaces(String xml)
    {
        StringBuffer response = new StringBuffer();
        int startElement = xml.indexOf("<");
        int endElement = xml.indexOf(">");
        int nextName = xml.indexOf(":");
        int nextSpace = xml.indexOf(" ");

        if (startElement < 0)
        {
            response.append(xml);
        }
        else
        {
            //we're within an element
            if (startElement < nextName && nextName < endElement)
            {
                //is there a space before the ':', indicating
                //possible namespace definition, if so we ignore
                if (nextSpace < 0 || nextSpace > nextName)
                {
                    int startoffset = 1;
                    if (xml.charAt(startElement + 1) == '/')
                    {
                        startoffset = 2;
                    }
                    response.append(xml.substring(0, startElement + startoffset));
                    response.append(xml.substring(nextName + 1, endElement + 1));
                }
                else
                {
                    response.append(xml.substring(0, endElement + 1));
                }
            }
            else
            {
                response.append(xml.substring(0, endElement + 1));
            }

            //move to next element
            xml = xml.substring(endElement + 1);
            int nextStartElement = xml.indexOf("<");
            if (nextStartElement >= 0)
            {
                response.append(xml.substring(0, nextStartElement));
                response.append(removeNamespaces(xml.substring(nextStartElement)));
            }
            else
            {
                response.append(xml);
            }
        }
        return response.toString();
    }
}
