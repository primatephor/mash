package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.apache.log4j.Logger;
import org.mash.harness.RunResponse;
import org.xml.sax.SAXException;

import java.util.*;

/**
 * Wrap a WebResponse for parsing by verifiers.  To retrieve special
 *
 * @since Jul 5, 2009
 */
public class HttpResponse implements RunResponse
{
    private static final Logger log = Logger.getLogger(HttpResponse.class);
    private String statusMessage;
    private int statusCode;
    private Page webPage;
    private SgmlPage sgmlPage;
    private Set<Cookie> cookies;

    public HttpResponse(Page webPage)
    {
        this(webPage, null);
    }

    public HttpResponse(final Page webPage, final Set<Cookie> cookies)
    {
        this.webPage = webPage;
        if(webPage instanceof SgmlPage)
        {
            sgmlPage = (SgmlPage) webPage;
        }
        this.statusMessage = webPage.getWebResponse().getStatusMessage();
        this.statusCode = webPage.getWebResponse().getStatusCode();
        this.cookies = cookies;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public int getStatusCode()
    {
        return statusCode;
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
        retrieveByElementName(name, results);

        if (results.size() == 0)
        {
            retrieveByXpath(name, results);
        }

        if (results.size() == 0)
        {
            retrieveCookies(name, results);
        }

        if (results.size() == 0)
        {
            log.warn("Unable to determine the value of node " + name);
        }
        return results;
    }

    private void retrieveByXpath(String name, Collection<String> results)
    {
        if(sgmlPage != null)
        {
            List<?> paths = sgmlPage.getByXPath(name);
            for (Object path : paths)
            {
                if (path instanceof DomAttr)
                {
                    DomAttr attr = (DomAttr) path;
                    log.debug("Found " + attr.getValue());
                    results.add(attr.getValue());
                }
                else if (path instanceof DomNode)
                {
                    DomNode node = (DomNode) path;
                    log.debug("Found " + node.asText());
                    results.add(node.asText());
                }
            }
        }
    }

    private void retrieveByElementName(String name, Collection<String> results)
    {
        if(sgmlPage != null)
        {
            Iterable<HtmlElement> iters = sgmlPage.getHtmlElementDescendants();
            for (HtmlElement iter : iters)
            {
                String value = null;
                String elementName = iter.getAttribute("name");
                if (elementName == null || elementName.length() == 0)
                {
                    elementName = iter.getAttribute("id");
                }

                if (elementName != null && elementName.equals(name))
                {
                    log.debug("Found element node :" + elementName);
                    value = iter.getAttribute("value");
                    if (value == null || value.length() == 0)
                    {
                        value = iter.getNodeValue();
                    }
                    if (value == null || value.length() == 0)
                    {
                        value = iter.getTextContent();
                    }
                }
                if (value != null && value.length() > 0)
                {
                    log.debug("SETTING '" + elementName + "' to value:" + value);
                    results.add(value);
                }
            }
        }
    }

    private void retrieveCookies(String name, Collection<String> results)
    {
        if (null != cookies)
        {
            for (Cookie cookie : cookies)
            {
                if (cookie.getName().equals(name))
                {
                    results.add(cookie.getValue());
                }
            }
        }
    }

    public Collection<String> getValues()
    {
        Collection<String> results = Collections.emptyList();
        try
        {
            if(sgmlPage != null)
            {
                Iterable<HtmlElement> elements = sgmlPage.getHtmlElementDescendants();
                for (HtmlElement element : elements)
                {
                    results.addAll(getValues(element.getNodeName()));
                }
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

    public Page getWebPage()
    {
        return webPage;
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append("Status:").append(this.webPage.getWebResponse().getStatusCode());
        if(sgmlPage != null)
        {
            if (this.sgmlPage.getFirstChild() != null)
            {
                result.append(": Start:\n").append(this.sgmlPage.getFirstChild().asXml()).append("\n");
            }
            if (this.sgmlPage.getTextContent() != null)
            {
                result.append(": Content:\n").append(this.sgmlPage.getTextContent()).append("\n");
            }
        }
        return result.toString();
    }
}
