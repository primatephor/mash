package org.mash.harness.rest;

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

    public Document getDocument(String xml)
    {
        Document document = null;
        ByteArrayInputStream inputStream = null;

        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(false);
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
        return document;
    }

    public String[] getPath(String doc, String path)
    {
        return getPath(getDocument(doc), path);
    }

    public String[] getPath(Document doc, String path)
    {
        String[] result = null;

        if (doc != null &&
            path != null)
        {
            try
            {
                XPathFactory xpFactory = XPathFactory.newInstance();
                XPath xpath = xpFactory.newXPath();
                XPathExpression expression = xpath.compile(path);
                NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);

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

    public String getPath(Document doc, String path, String delimiter)
    {
        String[] results = getPath(doc, path);
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

}
