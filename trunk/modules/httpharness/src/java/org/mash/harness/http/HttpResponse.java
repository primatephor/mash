package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.log4j.Logger;
import org.mash.harness.RunResponse;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Wrap a WebResponse for parsing by verifiers.  To retrieve special
 *
 * @author: teastlack
 * @since: Jul 5, 2009
 */
public class HttpResponse implements RunResponse
{
    private static final Logger log = Logger.getLogger(HttpResponse.class);
    private HtmlPage webResponse;

    public HttpResponse(HtmlPage webResponse)
    {
        this.webResponse = webResponse;
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
            results.addAll(retrieveFormElements(name));
        }
        catch (Exception e)
        {
            log.error("Problem retrieving elements with name " + name, e);
        }
        return results;
    }

    private Collection<String> retrieveFormElements(String name)
            throws SAXException
    {
        Collection<String> results = new ArrayList<String>();
        for (HtmlForm htmlForm : webResponse.getForms())
        {
            List<HtmlInput> input = htmlForm.getInputsByName(name);
            if (input != null && input.size() > 0)
            {
                log.debug("param text:" + input.get(0).asText());
                results.add(input.get(0).asText());
            }
        }
        return results;
    }

    private Collection<String> retrieveElements(String name)
            throws SAXException
    {
        List<HtmlElement> elements = webResponse.getElementsByName(name);
        Collection<String> results = new ArrayList<String>();
        for (HtmlElement element : elements)
        {
            String text = element.asText();
            if (text != null)
            {
                results.add(text);
            }
        }
        return results;
    }

    public Collection<String> getValues()
    {
        Collection<String> results = Collections.emptyList();
        try
        {
            Iterable<HtmlElement> elements = webResponse.getAllHtmlChildElements();
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
        if (webResponse != null)
        {
            result = webResponse.getTextContent();
        }
        return result;
    }

    public HtmlPage getWebResponse()
    {
        return webResponse;
    }
}
