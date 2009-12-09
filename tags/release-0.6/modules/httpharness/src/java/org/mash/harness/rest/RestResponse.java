package org.mash.harness.rest;

import org.mash.harness.http.HttpResponse;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

import java.util.Collection;
import java.util.Arrays;
import java.util.List;

/**
 * @author: teastlack
 * @since: Nov 22, 2009
 */
public class RestResponse extends HttpResponse
{
    private XmlPage page;

    public RestResponse(XmlPage page)
    {
        super(page);
        this.page = page;
    }

    public String getValue(String expression)
    {
        return page.getFirstByXPath(expression);
    }

    public Collection<String> getValues(String expression)
    {
        return (List<String>)page.getByXPath(expression); 
    }

    public Collection<String> getValues()
    {
        return Arrays.asList(getString());
    }

    public String getString()
    {
        return page.asXml();
    }

}
