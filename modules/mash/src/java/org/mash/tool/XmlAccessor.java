package org.mash.tool;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author
 * @since Sep 17, 2009 6:07:28 PM
 */
public class XmlAccessor
{
    private static final Logger log = Logger.getLogger(XmlAccessor.class.getName());
    private String xml;
    private Document document;

    private String root = "documents";
    private boolean addedRoot = false;

    public XmlAccessor(String xml)
    {
        this.xml = xml;
    }

    public Document getDocument()
    {
        if (document == null)
        {
            xml = removeNamespaces(xml);
            xml = removeXmlTags(xml);
            try
            {
                buildDocument(xml);
            }
            catch (Exception e)
            {
                log.error("Failed to parse xml", e);
            }
        }
        return document;
    }

    private void buildDocument(String xml) throws Exception
    {
        ByteArrayInputStream inputStream = null;
        try
        {
            log.debug("Building document from xml for accessing");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            docFactory.setValidating(false);
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            document = builder.parse(inputStream);
        }
        catch (SAXParseException e)
        {
            if (!addedRoot)
            {
                addedRoot = true;
                log.info("Problem parsing xml, adding root element to contain it");
                xml = "<" + root + ">" + xml + "</" + root + ">";
                buildDocument(xml);
            }
            else
            {
                throw e;
            }
        }
        finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch (IOException e)
            {
                log.error("Error when closing IOStream", e);
            }
        }
    }

    public String buildPath(String current)
    {
        String result = current;
        if (addedRoot)
        {
            result = "/" + root + current;
        }
        return result;
    }

    public String[] getPath(String path)
    {
        String[] result = null;

        if (getDocument() != null &&
            path != null)
        {
            try
            {
                XPathFactory xpFactory = XPathFactory.newInstance();
                XPath xpath = xpFactory.newXPath();
                String expressionPath = buildPath(path);
                log.debug("Searching for " + expressionPath);
                XPathExpression expression = xpath.compile(expressionPath);

                try
                {
                    NodeList nodes = (NodeList) expression.evaluate(getDocument(), XPathConstants.NODESET);
                    result = new String[nodes.getLength()];
                    for (int index = 0; index < result.length; index++)
                    {
                        result[index] = nodes.item(index).getTextContent();
                        log.trace("Found:"+result[index]);
                    }
                }
                catch (XPathExpressionException e)
                {
                    //this is for cases where the xpath query does not evaluate to a nodelist, i.e. "count(/foo/bar)"
                    String value = expression.evaluate(getDocument());
                    result = new String[]{value};
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

    private String removeXmlTags(String xml)
    {
        StringBuffer response = new StringBuffer();
        String start = "<?";
        String end = "?>";

        int startIndex = xml.indexOf(start);
        int endIndex = xml.indexOf(end);

        String remainder = xml;
        while (startIndex >= 0 && endIndex > 0)
        {
            response.append(remainder.substring(0, startIndex));
            remainder = remainder.substring(endIndex + 2);
            startIndex = remainder.indexOf(start);
            endIndex = remainder.indexOf(end);
        }
        response.append(remainder);

        return response.toString();
    }

    private String removeNamespaces(String xml)
    {
        return xml.replaceAll("(</?)[^:> ]+:", "$1");
    }
}
