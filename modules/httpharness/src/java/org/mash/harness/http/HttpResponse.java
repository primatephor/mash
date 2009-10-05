package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.log4j.Logger;
import org.mash.harness.RunResponse;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Wrap a WebResponse for parsing by verifiers.  To retrieve special
 *
 * @author: teastlack
 * @since: Jul 5, 2009
 */
public class HttpResponse implements RunResponse
{
    private static final Logger log = Logger.getLogger(HttpResponse.class);
    private HtmlPage webPage;

    public HttpResponse(HtmlPage webPage)
    {
        this.webPage = webPage;
    }

    public String getValue(String name)
    {
        String result = null;
        Collection<String> results = getValues(name);
        if (results.size() > 0)
        {
            result = results.iterator().next();
        }
        return result;
    }

    public Collection<String> getValues(String name)
    {
        Collection<String> results = new ArrayList<String>();
        try
        {
            results.addAll(retrieveElements(name));
        }
        catch (Exception e)
        {
            log.error("Problem retrieving elements with name " + name, e);
        }
        return results;
    }

    private Collection<String> retrieveElements(String name)
            throws SAXException
    {
        Collection<String> results = new ArrayList<String>();
        Iterable<HtmlElement> iters = webPage.getAllHtmlChildElements();
        for (HtmlElement iter : iters)
        {
            String elementName = iter.getAttribute("name");
            if (elementName != null && elementName.equals(name))
            {
                log.debug("Found element node :" + elementName +
                          " with value " + iter.getAttribute("value"));
                if (iter.getAttribute("value") != null)
                {
                    log.debug("SETTING " + elementName + " to attr " + iter.getAttribute("value"));
                    results.add(iter.getAttribute("value"));
                }
                else if (iter.getNodeValue() != null &&
                         iter.getNodeValue().length() > 0)
                {
                    log.debug("SETTING " + elementName + " to val " + iter.getNodeValue());
                    results.add(iter.getNodeValue());
                }
                else
                {
                    log.warn("Unable to determing the value of node " + iter.getAttribute("name"));
                }
                break;
            }
        }
        return results;
    }

    public Collection<String> getValues()
    {
        Collection<String> results = Collections.emptyList();
        try
        {
            Iterable<HtmlElement> elements = webPage.getAllHtmlChildElements();
            for (HtmlElement element : elements)
            {
                results.addAll(getValues(element.getNodeName()));
            }
        }
        catch (Exception e)
        {
            log.error("Problem retrieving elements", e);
        }
        return results;
    }

    public String getString()
    {
        String result = "";
        if (webPage != null)
        {
            result = webPage.getWebResponse().getContentAsString();
        }
        else
        {
            log.warn("Web page is null, unable to parse any content!");
        }
        return result;
    }

    public HtmlPage getWebPage()
    {
        return webPage;
    }
}