package org.mash.harness.rest;

import org.mash.harness.RunResponse;
import org.xml.sax.InputSource;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.meterware.httpunit.WebResponse;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathConstants;

/**
 * The rest response retrieves values from the response differently than the standard http response in that the name is
 * an xpath expression, so the response is expected to be an XML.
 *
 * @author: teastlack
 * @since: Jul 26, 2009
 */
public class RestResponse implements RunResponse
{
    private static final Logger LOG = Logger.getLogger(RestResponse.class.getName());
    private WebResponse webResponse;

    public RestResponse(WebResponse webResponse)
    {
        this.webResponse = webResponse;
    }

    public String getValue(String expression)
    {
        String evaluation = null;
        String response = getString();

        if (response != null)
        {
            XPath xPath = XPathFactory.newInstance().newXPath();
            InputSource inputSource = new InputSource();
            try
            {
                inputSource.setByteStream(new ByteArrayInputStream(response.getBytes()));
                XPathExpression xPathExpression = xPath.compile(expression);
                evaluation = xPathExpression.evaluate(inputSource);
            }
            catch (XPathExpressionException e)
            {
                LOG.error("Problem evaluating xpath", e);
            }
        }

        return evaluation;
    }

    public Collection<String> getValues(String name)
    {
        Collection<String> results = new ArrayList<String>();
        String response = getString();
        if (response != null)
        {
            InputSource inputSource = new InputSource();
            try
            {
                XPath xpath = XPathFactory.newInstance().newXPath();
                inputSource.setByteStream(new ByteArrayInputStream(response.getBytes()));
                DTMNodeList nodeSet = (DTMNodeList) xpath.evaluate(name, inputSource, XPathConstants.NODESET);
                if (nodeSet != null)
                {
                    for (int i = 0; i < nodeSet.getLength(); i++)
                    {
                        Node node = nodeSet.item(i);
                        results.add(node.getTextContent());
                    }
                }
            }
            catch (XPathExpressionException e)
            {
                LOG.error("Problem evaluating xpath", e);
            }
        }
        return results;
    }

    public String getString()
    {
        String response = null;
        try
        {
            if (webResponse != null)
            {
                response = webResponse.getText();
            }
        }
        catch (IOException e)
        {
            LOG.error("Problem retrieving reponse from web request", e);
        }
        return response;
    }
}
