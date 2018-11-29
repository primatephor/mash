package org.mash.harness.rest;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger log = LogManager.getLogger(RestResponse.class.getName());
    private XmlResponse response;

    public RestResponse(Page page)
    {
        super(page);

        /* Note that it's best to always do getContentAsString() here since that returns the raw content.
           Previously, this code used to do:

            if(page instanceof XmlPage)
            {
                response = new XmlResponse(((XmlPage)page).asXml());
            }

           The problem with this is that the GargoyleSoftware HttpUnit libraries return the XML
           formatted like this:

            <delivery>
              <shipper>
                <organization>
                  Development Merchant
                </organization>
              </shipper>
            </delivery>

           Instead of the correct:

            <delivery>
              <shipper>
                <organization>Development Merchant</organization>
              </shipper>
            </delivery>

           This means that later when we access specific elements in the XML, the value that is
           returned has leading and trailing whitespace and line breaks, since technically in XML the
           node value is *everything* between starting and ending tags.
         */

        response = new XmlResponse(page.getWebResponse().getContentAsString());
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
