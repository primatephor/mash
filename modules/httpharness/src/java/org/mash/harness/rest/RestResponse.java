package org.mash.harness.rest;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import org.mash.harness.RunResponse;
import org.mash.harness.XmlResponse;
import org.mash.harness.http.HttpResponse;

import java.util.Arrays;
import java.util.Collection;

/**
 * @since Nov 22, 2009
 */
public class RestResponse extends HttpResponse implements RunResponse
{
    private XmlResponse response;

    public RestResponse(Page page)
    {
        super(page);
        if(page instanceof XmlPage)
        {
            response = new XmlResponse(((XmlPage)page).asXml());
        }
        else if (page instanceof TextPage)
        {
            response = new XmlResponse(((TextPage)page).getContent());
        }
        else
        {
            response = new XmlResponse(page.getWebResponse().getContentAsString());
        }
    }

    public String getValue(String expression)
    {
        return response.getValue(expression);
    }

    public Collection<String> getValues(String expression)
    {
        return response.getValues(expression);
    }

    public Collection<String> getValues()
    {
        return Arrays.asList(getString());
    }

    public String getString()
    {
        return response.getString();
    }
}
