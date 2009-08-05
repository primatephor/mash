package org.mash.harness.http;

import com.meterware.httpunit.HTMLElement;
import com.meterware.httpunit.WebResponse;
import org.apache.log4j.Logger;
import org.mash.harness.RunResponse;

import java.io.IOException;
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
    private WebResponse webResponse;

    public HttpResponse(WebResponse webResponse)
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
        Collection<String> results = Collections.emptyList();
        try
        {
            HTMLElement[] elements = webResponse.getElementsWithName(name);
            results = new ArrayList<String>();
            for (HTMLElement element : elements)
            {
                String text = element.getText();
                if (text == null || text.length() == 0)
                {
                    text = element.getNode().getAttributes().getNamedItem("value").getNodeValue();
                }
                results.add(text);
            }
        }
        catch (Exception e)
        {
            log.error("Problem retrieving elements with name " + name, e);
        }
        return results;
    }

    public String getString()
    {
        String result = "";
        try
        {
            if (webResponse != null)
            {
                result = webResponse.getText();
            }
        }
        catch (IOException e)
        {
            log.error("Unexpected error retrieving response text", e);
        }
        return result;
    }

    public WebResponse getWebResponse()
    {
        return webResponse;
    }
}
